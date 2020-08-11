package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.commons.DefineUtils;
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
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.artisan.service.ArtisanService;
import com.ecomm.define.suppliers.commons.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
@Qualifier("artisanDataService")
public class GenerateBCArtisanDataServiceImpl implements GenerateBCDataService<ArtisanProduct> {

    private static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";
    private final Logger LOGGER = LoggerFactory.getLogger(GenerateBCArtisanDataServiceImpl.class);
    @Autowired
    private ArtisanService artisanService;

    @Autowired
    private BigCommerceApiService bigCommerceApiService;

    @Autowired
    private BigCommerceImageApiService bigCommerceImageApiService;

    @Autowired
    private BigcBrandApiRepository brandApiRepository;
    @Autowired
    private MongoOperations mongoOperations;

    @Value("${bigcommerce.f2g.profit.limit.high}")
    private String higherLimitHDPrice;

    @Value("${bigcommerce.f2g.profit.percentage.low}")
    private String percentageLow;

    @Override
    public void generateBcProductsFromSupplier(List<ArtisanProduct> productList) throws Exception {
        //Process Discontinued catalog
        processDiscontinuedCatalog(productList);

        //Process updated catalog, if there is any updates available in price & stock & images.
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<ArtisanProduct> updatedCatalogList = productList
                .stream()
                .filter(ArtisanProduct::isUpdated)
                .collect(Collectors.toList());
        for (ArtisanProduct artisanProduct : updatedCatalogList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("sku").is(BcConstants.ARTISAN + artisanProduct.getSku()));
            BcProductData byProductSku = mongoOperations.findOne(query, BcProductData.class);

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(artisanProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(artisanProduct.getProductName()));

                byProductSku.setSku(BcConstants.ARTISAN + artisanProduct.getSku());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + artisanProduct.getProductName() + " " + artisanProduct.getBp1());
                StringBuilder discriptionBuilder = new StringBuilder("");
                discriptionBuilder.append(artisanProduct.getDescription());
                discriptionBuilder.append("Dimensions - (");


                byProductSku.setSupplier(Supplier.ARTISAN.getName());
                byProductSku.setType(BcConstants.TYPE);
                if (artisanProduct.getWeight() != null) {
                    int weight = artisanProduct.getWeight().intValue();
                    byProductSku.setWeight(weight);
                    discriptionBuilder.append(" Weight : " + weight + "kg");
                }
                if (artisanProduct.getHeight() != null) {
                    int height = artisanProduct.getHeight().intValue();
                    byProductSku.setHeight(height);
                    discriptionBuilder.append(" Height : " + height + "mm");
                }
                if (artisanProduct.getWidth() != null) {
                    int width = artisanProduct.getWidth().intValue();
                    byProductSku.setWidth(width);
                    discriptionBuilder.append(" Width : " + width + "mm");
                }
                if (artisanProduct.getDepth() != null) {
                    int depth = artisanProduct.getDepth().intValue();
                    byProductSku.setDepth(depth);
                    discriptionBuilder.append(" Depth : " + depth + "mm)");
                }
                discriptionBuilder.append("Assembly Instructions - " + artisanProduct.getAssemblyInstructions());

                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                byProductSku.setDescription(artisanProduct.getDescription());
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + artisanProduct.getProductName() + " " + artisanProduct.getBp1());
                setPriceAndQuantity(artisanProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(artisanProduct.getProductName()));

                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        }
        updateBigCommerceProducts(updatedBcProductDataList);
    }


    private void processDiscontinuedCatalog(List<ArtisanProduct> productList) throws URISyntaxException {
        List<ArtisanProduct> discontinuedList = productList
                .stream()
                .filter(ArtisanProduct::isDiscontinued)
                .collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());

        Query query = new Query();
        for (ArtisanProduct artisanProduct : discontinuedList) {
            query.addCriteria(Criteria.where("sku").is(BcConstants.ARTISAN + artisanProduct.getSku()));
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
                    LOGGER.info("Successfully sent Artisan Product to Big Commerce for the product id {}, and sku {}", resultData.getId(), resultData.getSku());
                } else {
                    String url = uri + "/" + product.getId();
                    ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, BigCommerceApiProduct.class);
                    BcProductData data = Objects.requireNonNull(responseEntity.getBody()).getData();
                    LOGGER.info("Successfully updated the Artisan Product on Big Commerce for the product id {}, and sku {}", data.getId(), data.getSku());
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
        LOGGER.info("Successfully Updated Artisan Catalog to BigCommerce");
    }

    private void updateImage(BcProductData data, RestTemplate restTemplate) throws Exception {
        Optional<ArtisanProduct> byProductSku = artisanService.findByProductSku(data.getSku());
        if (byProductSku.isPresent()) {
            ArtisanProduct artisanProduct = byProductSku.get();
            List<String> imagesList = artisanProduct.getImages();

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


    private void setPriceAndQuantity(ArtisanProduct artisanProduct, BcProductData byProductSku) {
        evaluatePrice(artisanProduct, byProductSku);
        byProductSku.setInventoryLevel(Math.max(artisanProduct.getStockLevel(), 0));
        byProductSku.setAvailability(BcConstants.PREORDER);
        byProductSku.setAvailabilityDescription("Usually dispatches in 6 to 8 weeks.");
        if (artisanProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
            byProductSku.setAvailabilityDescription("Usually dispatches in 10 to 12 working days.");
        }
    }

    private void evaluatePrice(ArtisanProduct artisanProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = artisanProduct.getPrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
            if (originalPrice.compareTo(new BigDecimal(higherLimitHDPrice)) > 0) {
                BigDecimal retailPrice = originalPrice.add(DefineUtils.percentage(originalPrice, new BigDecimal(percentageLow)));
                byProductSku.setRetailPrice(retailPrice.intValue());
            }
        }
    }
}