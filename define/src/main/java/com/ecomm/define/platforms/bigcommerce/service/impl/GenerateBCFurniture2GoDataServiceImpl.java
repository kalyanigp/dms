package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageDataList;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiImage;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiProduct;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceImageApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.platforms.commons.BCUtils;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import com.ecomm.define.suppliers.furniture2go.service.Furniture2GoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

import static com.ecomm.define.commons.DefineUtils.evaluateDimensions;
import static com.ecomm.define.commons.DefineUtils.getDelimiter;
import static com.ecomm.define.platforms.bigcommerce.service.impl.GenerateBCMaisonDataServiceImpl.PRODUCTS_ENDPOINT;

/**
 * Created by vamshikirangullapelly on 18/07/2020.
 */
@Service
@Qualifier("furniture2GoDataService")
public class GenerateBCFurniture2GoDataServiceImpl implements GenerateBCDataService<Furniture2GoProduct> {


    @Autowired
    BigCommerceService bigCommerceService;
    @Autowired
    BigCommerceApiService bigCommerceApiService;
    @Autowired
    BigCommerceImageApiService bigCommerceImageApiService;
    @Autowired
    BigCommerceProductApiController bigCommerceProductApiController;
    @Autowired
    BigcBrandApiRepository brandApiRepository;

    @Autowired
    Furniture2GoService furniture2GoService;

    @Value("${bigcommerce.f2g.profit.limit.high}")
    private String higherLimitHDPrice;

    @Value("${bigcommerce.f2g.profit.percentage.low}")
    private String percentageLow;

    private final Logger LOGGER = LoggerFactory.getLogger(BigCommerceProductApiController.class);


    @Override
    public void generateBcProductsFromSupplier(List<Furniture2GoProduct> productList) throws Exception {
        //Process Discontinued catalog
        processDiscontinuedCatalog(productList);

        //Process updated catalog, if there is any updates available in price & stock & images.
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        List<Furniture2GoProduct> updatedCatalogList = productList
                .stream()
                .filter(Furniture2GoProduct::isUpdated)
                .collect(Collectors.toList());
        for (Furniture2GoProduct furniture2GoProduct : updatedCatalogList) {
            BcProductData byProductSku = bigCommerceApiService.findByProductSku(furniture2GoProduct.getSku());

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(furniture2GoProduct.getDescription()));
                byProductSku.setSku(furniture2GoProduct.getSku());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + furniture2GoProduct.getProductName() + " " + furniture2GoProduct.getFinish());

                byProductSku.setSupplier(Supplier.FURNITURE2GO.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(furniture2GoProduct.getWeight().intValue());
                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                Optional<BcBrandData> byName = brandApiRepository.findByName(Supplier.SELLER_BRAND.getName());
                if (byName.isPresent()) {
                    byProductSku.setBrandId(byName.get().getId());
                }
                evaluateDescription(furniture2GoProduct, byProductSku);
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + furniture2GoProduct.getProductName() + " " + furniture2GoProduct.getFinish());
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                evaluateDescription(furniture2GoProduct, byProductSku);
                byProductSku.setCategories(BCUtils.assignCategories(furniture2GoProduct.getDescription()));
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        }
        updateBigCommerceProducts(updatedBcProductDataList);
    }

    /**
     * This method will delete the discontinued catalog from BigCommerce and from bcProductData table
     *
     * @param productList
     * @throws URISyntaxException
     */
    private void processDiscontinuedCatalog(List<Furniture2GoProduct> productList) throws URISyntaxException {
        List<Furniture2GoProduct> discontinuedList = productList
                .stream()
                .filter(Furniture2GoProduct::isDiscontinued)
                .collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());

        for (Furniture2GoProduct furniture2GoProduct : discontinuedList) {
            BcProductData byProductSku = bigCommerceApiService.findByProductSku(furniture2GoProduct.getSku());
            if (byProductSku.getId() != null) {
                String url = uri + "/" + byProductSku.getId();
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
                bigCommerceApiService.delete(byProductSku.get_id());
                LOGGER.info("Successfully Deleted product from Big Commerce due to discontinue, product id {} and product sku {}", byProductSku.getId(), byProductSku.getSku());
            }
        }
    }

    /**
     * This method will update the catalog if there is any modifications in the price & stock level
     *
     * @param updatedBcProductDataList
     * @throws Exception
     */
    private void updateBigCommerceProducts(List<BcProductData> updatedBcProductDataList) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        List<BcProductData> duplicateRecords = new ArrayList<>();

        for (BcProductData product : updatedBcProductDataList) {
            populateBigCommerceProduct(restTemplate, uri, duplicateRecords, product);
        }
        if (!duplicateRecords.isEmpty()) {
            LOGGER.info("Found duplicate products while processing Furniture2Go products. Processing the duplicate products by updating the name attribute");
        }
        LOGGER.info("Successfully Pushed Furniture2Go Products to Commerce");
    }

    private void populateBigCommerceProduct(RestTemplate restTemplate, URI uri, List<BcProductData> duplicateRecords, BcProductData product) {
        BigCommerceApiProduct result;
        HttpEntity<BcProductData> request = new HttpEntity<>(product, bigCommerceApiService.getHttpHeaders());
        try {
            if (product.getId() == null) {
                LOGGER.info(Objects.requireNonNull(request.getBody()).getName());
                result = restTemplate.postForObject(uri, request, BigCommerceApiProduct.class);
                BcProductData resultData = Objects.requireNonNull(result).getData();
                resultData.set_id(product.get_id());
                resultData = bigCommerceApiService.update(resultData);
                updateImage(resultData, restTemplate);
                LOGGER.info("Successfully Created Furniture2Go Product in to Big Commerce for the product id {}, sku {} & name {}", resultData.getId(), resultData.getSku(), resultData.getName());
            } else {
                String url = uri + "/" + product.getId();
                ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, BigCommerceApiProduct.class);
                BcProductData resultData = Objects.requireNonNull(responseEntity.getBody()).getData();
                LOGGER.info("Successfully Updated Furniture2Go Product in to Big Commerce for the product id {}, sku {} & name {}", resultData.getId(), resultData.getSku(), resultData.getName());
            }
        } catch (Exception ex) {
            LOGGER.error("Error while updating the Furniture2Go inventory {}", Objects.requireNonNull(request.getBody()).getName());
            duplicateRecords.add(product);
        }
    }

    private BcProductData setPriceAndQuantity(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        evaluatePrice(furniture2GoProduct, byProductSku);
        byProductSku.setInventoryLevel(Math.max(furniture2GoProduct.getStockLevel(), 0));
        byProductSku.setAvailability(BcConstants.PREORDER);
        if (furniture2GoProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
        }
        return byProductSku;
    }

    private void evaluatePrice(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = furniture2GoProduct.getPrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
            if (originalPrice.compareTo(new BigDecimal(higherLimitHDPrice)) > 0) {
                BigDecimal retailPrice = originalPrice.add(DefineUtils.percentage(originalPrice, new BigDecimal(percentageLow)));
                byProductSku.setRetailPrice(retailPrice.intValue());
            }
        }
    }

    private void evaluateDescription(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        //Height logic
        StringBuilder dimensionsDescription = new StringBuilder();
        String heightDelimeter = getDelimiter(furniture2GoProduct.getHeight());
        dimensionsDescription.append(" Height :" + furniture2GoProduct.getHeight() + "(mm) ");
        if (!heightDelimeter.isEmpty()) {
            List<String> heightList = evaluateDimensions(heightDelimeter, furniture2GoProduct.getHeight());
            if (!heightList.isEmpty()) {
                byProductSku.setHeight(Integer.parseInt(heightList.get(0)));
            }
        } else {
            byProductSku.setHeight(new BigDecimal(furniture2GoProduct.getHeight()).intValue());
        }

        //Width logic
        String widthDelimeter = getDelimiter(furniture2GoProduct.getWidth());
        dimensionsDescription.append(" Width :" + furniture2GoProduct.getWidth() + "(mm) ");
        if (!widthDelimeter.isEmpty()) {
            List<String> widthList = evaluateDimensions(widthDelimeter, furniture2GoProduct.getWidth());
            if (!widthList.isEmpty()) {
                byProductSku.setWidth(Integer.parseInt(widthList.get(0)));
            }
        } else {
            byProductSku.setWidth(new BigDecimal(furniture2GoProduct.getWidth()).intValue());
        }

        //Depth logic
        String depthDelimeter = getDelimiter(furniture2GoProduct.getDepth());
        dimensionsDescription.append(" Depth :" + furniture2GoProduct.getDepth() + "(mm) ");
        if (!depthDelimeter.isEmpty()) {
            List<String> depthList = evaluateDimensions(widthDelimeter, furniture2GoProduct.getDepth());
            if (!depthList.isEmpty()) {
                byProductSku.setDepth(Integer.parseInt(depthList.get(0)));
            }
        } else {
            byProductSku.setDepth(new BigDecimal(furniture2GoProduct.getDepth()).intValue());
        }
        String stockArrivalDate = furniture2GoProduct.getStockArrivalDate();
        dimensionsDescription.append("Next Dispatch Date : ");
        if (stockArrivalDate != null && !stockArrivalDate.isEmpty()) {
            if (DefineUtils.isValidDate(stockArrivalDate)) {
                dimensionsDescription.append(stockArrivalDate);
            } else {
                dimensionsDescription.append(DefineUtils.plusDays(70));
            }
        } else {
            dimensionsDescription.append("Usually dispatches in next 10 working days");
        }
        byProductSku.setDescription(furniture2GoProduct.getDescription() + " " + dimensionsDescription.toString());
    }

    private void updateImage(BcProductData data, RestTemplate restTemplate) throws Exception {
        Optional<Furniture2GoProduct> byProductSku = furniture2GoService.findByProductSku(data.getSku());
        if (byProductSku.isPresent()) {
            Furniture2GoProduct furniture2GoProduct = byProductSku.get();
            List<String> imagesList = furniture2GoProduct.getImages();

            URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT + "/" + data.getId() + "/images");
            if (checkIfImagesExists(uri, restTemplate)) {
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

    private boolean checkIfImagesExists(URI uri, RestTemplate restTemplate) {
        ResponseEntity<BcProductImageDataList> responseEntity;
        HttpEntity<BcProductImageDataList> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BcProductImageDataList.class);
        List<BcProductImageData> bcCategoryDataList = Objects.requireNonNull(responseEntity.getBody()).getData();
        return bcCategoryDataList.isEmpty();
    }

}
