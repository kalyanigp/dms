package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageDataList;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiImage;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiProduct;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceImageApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import com.ecomm.define.suppliers.hillinterior.service.HillInteriorService;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
@Qualifier("hillInteriorDataService")
public class GenerateBCHillInteriorDataServiceImpl implements GenerateBCDataService<HillInteriorProduct> {

    private static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";
    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCHillInteriorDataServiceImpl.class);

    private final HillInteriorService hillInteriorService;

    private final BigCommerceApiService bigCommerceApiService;

    private final BigCommerceImageApiService bigCommerceImageApiService;

    private final BigcBrandApiRepository brandApiRepository;

    private final MongoOperations mongoOperations;

    @Autowired
    public GenerateBCHillInteriorDataServiceImpl(HillInteriorService hillInteriorService, BigCommerceApiService bigCommerceApiService, BigCommerceImageApiService bigCommerceImageApiService, BigcBrandApiRepository brandApiRepository, MongoOperations mongoOperations) {
        this.hillInteriorService = hillInteriorService;
        this.bigCommerceApiService = bigCommerceApiService;
        this.bigCommerceImageApiService = bigCommerceImageApiService;
        this.brandApiRepository = brandApiRepository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void generateBcProductsFromSupplier(List<HillInteriorProduct> productList) throws Exception {
        //Process Discontinued catalog
        processDiscontinuedCatalog(productList);

        //Process updated catalog, if there is any updates available in price & stock & images.
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<HillInteriorProduct> updatedCatalogList = productList
                .stream()
                .filter(HillInteriorProduct::isUpdated)
                .collect(Collectors.toList());


        updatedCatalogList.parallelStream().forEach(hillInteriorProduct -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(BcConstants.HILL_INTERIOR + hillInteriorProduct.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(hillInteriorProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(hillInteriorProduct.getProductName()));

                byProductSku.setSku(BcConstants.HILL_INTERIOR + hillInteriorProduct.getSku());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + hillInteriorProduct.getProductName());
                StringBuilder discriptionBuilder = new StringBuilder();
                if (hillInteriorProduct.getDescription() != null && !hillInteriorProduct.getDescription().isEmpty()) {
                    discriptionBuilder.append(hillInteriorProduct.getDescription());
                }
                discriptionBuilder.append("Dimensions - (");

                byProductSku.setSupplier(Supplier.HILL_INTERIORS.getName());
                byProductSku.setType(BcConstants.TYPE);
                if (hillInteriorProduct.getWeight() != null) {
                    int weight = hillInteriorProduct.getWeight().intValue();
                    byProductSku.setWeight(weight);
                    discriptionBuilder.append(" Weight : ").append(weight).append("kg");
                }
                if (hillInteriorProduct.getHeight() != null) {
                    int height = hillInteriorProduct.getHeight().intValue();
                    byProductSku.setHeight(height);
                    discriptionBuilder.append(" Height : ").append(height).append("mm");
                }
                if (hillInteriorProduct.getWidth() != null) {
                    int width = hillInteriorProduct.getWidth().intValue();
                    byProductSku.setWidth(width);
                    discriptionBuilder.append(" Width : ").append(width).append("mm");
                }
                if (hillInteriorProduct.getDepth() != null) {
                    int depth = hillInteriorProduct.getDepth().intValue();
                    byProductSku.setDepth(depth);
                    discriptionBuilder.append(" Depth : ").append(depth).append("mm)");
                }
                if (hillInteriorProduct.getFinish() != null && !hillInteriorProduct.getFinish().isEmpty()) {
                    discriptionBuilder.append("Finish - ").append(hillInteriorProduct.getFinish());
                }
                if (hillInteriorProduct.getColour() != null && !hillInteriorProduct.getColour().isEmpty()) {
                    discriptionBuilder.append("Colour - ").append(hillInteriorProduct.getFinish());
                }

                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                byProductSku.setDescription(discriptionBuilder.toString());
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + hillInteriorProduct.getProductName());
                setPriceAndQuantity(hillInteriorProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(hillInteriorProduct.getProductName()));

                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }

        });
        updateBigCommerceProducts(updatedBcProductDataList);
    }


    private void processDiscontinuedCatalog(List<HillInteriorProduct> productList) throws URISyntaxException {
        List<HillInteriorProduct> discontinuedList = productList
                .stream()
                .filter(HillInteriorProduct::isDiscontinued)
                .collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());

        Query query = new Query();
        for (HillInteriorProduct hillInteriorProduct : discontinuedList) {
            query.addCriteria(Criteria.where("sku").is(BcConstants.HILL_INTERIOR + hillInteriorProduct.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);
            if (Objects.requireNonNull(byProductSku).getId() != null) {
                String url = uri + "/" + byProductSku.getId();
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
                bigCommerceApiService.delete(byProductSku.get_id());
                LOGGER.info("Successfully Deleted product from Big Commerce due to discontinue, product id {} and product sku {}", byProductSku.getId(), byProductSku.getSku());
            }
        }
    }

    private void updateBigCommerceProducts(List<BcProductData> updatedBcProductDataList) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        List<BcProductData> duplicateRecords = new ArrayList<>();
        HttpEntity<BcProductData> request = null;
        BigCommerceApiProduct result;
        for (BcProductData product : updatedBcProductDataList) {
            try {
                request = new HttpEntity<>(product, bigCommerceApiService.getHttpHeaders());
                if (product.getId() == null) {
                    LOGGER.info(Objects.requireNonNull(request.getBody()).getName());
                    result = restTemplate.postForObject(uri, request, BigCommerceApiProduct.class);
                    BcProductData resultData = Objects.requireNonNull(result).getData();
                    resultData.set_id(product.get_id());
                    resultData = bigCommerceApiService.update(resultData);
                    updateImage(resultData, restTemplate);
                    LOGGER.info("Successfully sent HillInterior Product to Big Commerce for the product id {}, and sku {}", resultData.getId(), resultData.getSku());
                } else {
                    String url = uri + "/" + product.getId();
                    ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, BigCommerceApiProduct.class);
                    BcProductData data = Objects.requireNonNull(responseEntity.getBody()).getData();
                    LOGGER.info("Successfully updated the HillInterior Product on Big Commerce for the product id {}, and sku {}", data.getId(), data.getSku());
                }
            } catch (Exception duplicateRecordException) {
                LOGGER.error("Duplicate record found with the name {}", Objects.requireNonNull(request.getBody()).getName());
                duplicateRecords.add(product);
                continue;
            }
        }
        if (!duplicateRecords.isEmpty()) {
            LOGGER.info("Found duplicate products while processing maison products. Processing the duplicate products by updating the name attribute");
        }
        LOGGER.info("Successfully Updated HillInterior Catalog to BigCommerce");
    }

    private void updateImage(BcProductData data, RestTemplate restTemplate) throws Exception {
        Optional<HillInteriorProduct> byProductSku = hillInteriorService.findByProductSku(data.getSku());
        if (byProductSku.isPresent()) {
            HillInteriorProduct hillInteriorProduct = byProductSku.get();
            List<String> imagesList = hillInteriorProduct.getImages();

            URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT + "/" + data.getId() + "/images");
            if (checkImagesNotExists(uri, restTemplate)) {
                BcProductImageData imageData;
                HttpEntity<BcProductImageData> request;
                boolean ifFirstImage = true;
                int sortOrder = 1;
                int imageDesriptionCount = 1;
                BigCommerceApiImage bigCommerceApiImage;
                List<String> filteredImagesList = imagesList.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
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
    }

    private boolean checkImagesNotExists(URI uri, RestTemplate restTemplate) {
        ResponseEntity<BcProductImageDataList> responseEntity;
        HttpEntity<BcProductImageDataList> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BcProductImageDataList.class);
        List<BcProductImageData> bcCategoryDataList = Objects.requireNonNull(responseEntity.getBody()).getData();
        return bcCategoryDataList.isEmpty();
    }


    private void setPriceAndQuantity(HillInteriorProduct hillInteriorProduct, BcProductData byProductSku) {
        evaluatePrice(hillInteriorProduct, byProductSku);
        byProductSku.setInventoryLevel(Math.max(hillInteriorProduct.getStockLevel(), 0));
        byProductSku.setAvailability(BcConstants.PREORDER);
        byProductSku.setAvailabilityDescription("Usually dispatches on or after " + hillInteriorProduct.getStockExpectedOn());
        if (hillInteriorProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
            byProductSku.setAvailabilityDescription("Usually dispatches in 5 to 7 working days.");
        }
    }

    private void evaluatePrice(HillInteriorProduct hillInteriorProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = hillInteriorProduct.getPrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
        }
    }
}