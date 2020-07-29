package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiImage;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiProduct;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceImageApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.maison.domain.MaisonProduct;
import com.ecomm.define.suppliers.maison.service.MaisonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
@Qualifier("maisonDataService")
public class GenerateBCMaisonDataServiceImpl implements GenerateBCDataService<MaisonProduct> {

    public static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";
    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCMaisonDataServiceImpl.class);
    private final MaisonService maisonService;
    private final BigCommerceApiService bigCommerceApiService;
    private final BigCommerceImageApiService bigCommerceImageApiService;
    private final BigcBrandApiRepository brandApiRepository;
    private final MongoOperations mongoOperations;

    @Autowired
    public GenerateBCMaisonDataServiceImpl(MaisonService maisonService, BigCommerceApiService bigCommerceApiService
            , BigCommerceImageApiService bigCommerceImageApiService, BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.maisonService = maisonService;
        this.bigCommerceApiService = bigCommerceApiService;
        this.bigCommerceImageApiService = bigCommerceImageApiService;
        this.mongoOperations = mongoOperations;
        this.brandApiRepository = brandApiRepository;
    }


    @Override
    public void generateBcProductsFromSupplier(List<MaisonProduct> updatedMaisonProductList) throws Exception {
        //Process Discontinued catalog
        processDiscontinuedCatalog(updatedMaisonProductList);

        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<MaisonProduct> updatedCatalogList = updatedMaisonProductList
                .stream()
                .filter(MaisonProduct::isUpdated)
                .collect(Collectors.toList());

        updatedCatalogList.parallelStream().forEach(maisonProd -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(maisonProd.getProductCode()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(maisonProd, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories( maisonProd.getTitle()));
                byProductSku.setSku(maisonProd.getProductCode());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + maisonProd.getTitle());

                byProductSku.setSupplier(Supplier.MAISON.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(0);
                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                byProductSku.setAvailability(BcConstants.PREORDER);
                if (maisonProd.getStockQuantity() > 0) {
                    byProductSku.setAvailability(BcConstants.AVAILABLE);
                }

                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                if (maisonProd.getMaterial() != null) {
                    int index = 0;
                    if (maisonProd.getMaterial().contains("Kg")) {
                        index = maisonProd.getMaterial().indexOf("Kg");
                    } else if (maisonProd.getMaterial().contains("KG")) {
                        index = maisonProd.getMaterial().indexOf("KG");
                    }
                    if (index > 0) {
                        String weight = maisonProd.getMaterial().substring(index - 3, index);
                        weight = weight.replaceAll(" ", "").replaceAll(":", "");
                        double dWeight = Double.parseDouble(weight);
                        if ((dWeight == Math.ceil(dWeight)) && !Double.isInfinite(dWeight)) {
                            byProductSku.setWeight((int) dWeight);
                        }

                    }
                }
                if (maisonProd.getMaterial() != null) {
                    byProductSku.setDescription(maisonProd.getMaterial().replaceAll(",", ""));
                }
                byProductSku.setDescription(byProductSku.getDescription() + " " + getDimensions(maisonProd.getSize()));
                byProductSku.setAvailabilityDescription(getProductAvailability(Double.parseDouble(maisonProd.getTradePrice()), maisonProd.getStockQuantity()));
                byProductSku.setAvailability(getProductAvailability(Double.parseDouble(maisonProd.getTradePrice()), maisonProd.getStockQuantity()));

                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + maisonProd.getTitle());
                setPriceAndQuantity(maisonProd, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(maisonProd.getTitle()));
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        });
        updateBigCommerceProducts(updatedBcProductDataList);
    }

    private void setPriceAndQuantity(MaisonProduct maisonProd, BcProductData byProductSku) {
        int priceIntValue = evaluatePrice(maisonProd);
        byProductSku.setPrice(priceIntValue);
        byProductSku.setSalePrice(priceIntValue);
        byProductSku.setInventoryLevel(maisonProd.getStockQuantity() < 0 ? 0 : maisonProd.getStockQuantity());
    }

    private void updateBigCommerceProducts(List<BcProductData> updatedBcProductDataList) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        List<BcProductData> duplicateRecords = new ArrayList<>();
        AtomicReference<HttpEntity<BcProductData>> request = new AtomicReference<>();
        AtomicReference<BigCommerceApiProduct> result = new AtomicReference<>();

        updatedBcProductDataList.parallelStream().forEach(product -> {
            try {
                Objects.requireNonNull(request).set(new HttpEntity<>(product, bigCommerceApiService.getHttpHeaders()));
                if (product.getId() == null) {
                    LOGGER.info(Objects.requireNonNull(request.get().getBody()).getName());
                    Objects.requireNonNull(result).set(restTemplate.postForObject(uri, request, BigCommerceApiProduct.class));
                    BcProductData resultData = result.get().getData();
                    resultData.set_id(product.get_id());
                    resultData = bigCommerceApiService.update(resultData);
                    updateImage(resultData, restTemplate);
                    LOGGER.info("Successfully sent Maison Product to Big Commerce for the product id {}, and sku {}", resultData.getId(), resultData.getSku());
                } else {
                    String url = uri + "/" + product.getId();
                    ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request.get(), BigCommerceApiProduct.class);
                    BcProductData data = Objects.requireNonNull(responseEntity.getBody()).getData();
                    LOGGER.info("Successfully updated the Maison Product on Big Commerce for the product id {}, and sku {}", data.getId(), data.getSku());
                }
            } catch (Exception duplicateRecordException) {
                duplicateRecords.add(product);
            }

        });

        if (!duplicateRecords.isEmpty()) {
            duplicateRecords.forEach(record -> LOGGER.info("Found duplicate product with sku {} and product name {} ", record.getSku(), record.getName()));
        }
        LOGGER.info("Successfully Updated Maison Products to Big Commerce");
    }


    private int evaluatePrice(MaisonProduct maisonProd) {
        BigDecimal decimalPrice = null;
        if (StringUtils.isEmpty(maisonProd.getMspPrice()) || "N/A".equals(maisonProd.getMspPrice())) {
            if (!StringUtils.isEmpty(maisonProd.getTradePrice())) {
                decimalPrice = new BigDecimal(maisonProd.getTradePrice());
                decimalPrice = decimalPrice.multiply(BigDecimal.valueOf(2));
            }
        } else {
            decimalPrice = new BigDecimal(maisonProd.getMspPrice());
        }
        decimalPrice = Objects.requireNonNull(decimalPrice).add(new BigDecimal(1));
        return decimalPrice.intValue();

    }


    private String getDimensions(String size) {
        StringTokenizer st = new StringTokenizer(size, "x");
        StringBuilder stringBuilder = new StringBuilder();
        while (st.hasMoreTokens()) {
            String nextString = st.nextToken();
            if (nextString.contains("H")) {
                stringBuilder.append(nextString.replaceAll("H", "")).append("cm ");
            } else if (nextString.contains("W")) {
                stringBuilder.append(nextString.replaceAll("W", "")).append("cm ");
            } else if (nextString.contains("D")) {
                stringBuilder.append(nextString.replaceAll("D", "")).append("cm ");
            }
        }
        return stringBuilder.toString();
    }

    private String getProductAvailability(double tradePrice, int stockQty) {
        if (stockQty > 0) {
            if (tradePrice <= 50) {
                return "Usually dispatches in 3 days";
            } else if (tradePrice <= 150) {
                return "Usually dispatches in 5 days";
            } else if (tradePrice <= 300) {
                return "Usually dispatches in 10 days";

            } else if (tradePrice <= 700) {
                return "Usually dispatches in 15 days";

            } else if (tradePrice > 700) {
                return "Usually dispatches in 25 days";
            }
        }
        return "Your Order will be considered as Back Order. Kindly contact us for delivery timelines";
    }

    private void updateImage(BcProductData data, RestTemplate restTemplate) throws Exception {
        Optional<MaisonProduct> maisonProduct = maisonService.findByProductSku(data.getSku());
        if (maisonProduct.isPresent()) {
            MaisonProduct product = maisonProduct.get();
            List<String> images = Arrays.asList(product.getImages().split(","));
            URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT + "/" + data.getId() + "/images");
            BcProductImageData imageData;
            HttpEntity<BcProductImageData> request;
            boolean ifFirstImage = true;
            int sortOrder = 1;
            int imageDesriptionCount = 1;
            BigCommerceApiImage bigCommerceApiImage;
            List<String> filteredImagesList = images.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            for (String image : filteredImagesList) {
                try {
                    imageData = new BcProductImageData();
                    imageData.setId(data.getId());
                    imageData.setSortOrder(sortOrder++);
                    imageData.setIsThumbnail(ifFirstImage);
                    imageData.setDescription("Image_" + imageDesriptionCount++);
                    imageData.setImageUrl(image);
                    request = new HttpEntity<>(imageData, bigCommerceApiService.getHttpHeaders());
                    bigCommerceApiImage = restTemplate.postForObject(uri, request, BigCommerceApiImage.class);
                    bigCommerceImageApiService.create(Objects.requireNonNull(bigCommerceApiImage).getData());
                    ifFirstImage = false;
                } catch (Exception ex) {
                    LOGGER.error("Exception occurred while processing images for the product id {}", data.getId());
                    continue;
                }
            }
        }
    }


    private void processDiscontinuedCatalog(List<MaisonProduct> productList) throws URISyntaxException {
        List<MaisonProduct> discontinuedList = productList
                .stream()
                .filter(MaisonProduct::isDiscontinued)
                .collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());

        Query query = new Query();
        for (MaisonProduct artisanProduct : discontinuedList) {
            query.addCriteria(Criteria.where("sku").is(artisanProduct.getProductCode()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);
            if (Objects.requireNonNull(byProductSku).getId() != null) {
                String url = uri + "/" + byProductSku.getId();
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
                bigCommerceApiService.delete(byProductSku.get_id());
                LOGGER.info("Successfully Deleted product from Big Commerce due to discontinue, product id {} and product sku {}", byProductSku.getId(), byProductSku.getSku());
            }
        }
    }

}