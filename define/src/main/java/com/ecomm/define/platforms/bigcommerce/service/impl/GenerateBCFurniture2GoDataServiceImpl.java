package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiImage;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiProduct;
import com.ecomm.define.platforms.bigcommerce.ennum.Category;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceImageApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import com.ecomm.define.suppliers.furniture2go.service.Furniture2GoService;
import com.ecomm.define.suppliers.maison.domain.MaisonProduct;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private final Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);


    @Override
    public void generateBcProductsFromSupplier(List<Furniture2GoProduct> productList) throws Exception {
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        for (Furniture2GoProduct furniture2GoProduct : productList) {
            BcProductData byProductSku = bigCommerceApiService.findByProductSku(furniture2GoProduct.getSku());

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                assignCategories(byProductSku, furniture2GoProduct.getDescription());
                byProductSku.setSku(furniture2GoProduct.getSku());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + furniture2GoProduct.getProductName());

                byProductSku.setSupplier(Supplier.FURNITURE2GO.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(furniture2GoProduct.getWeight().intValue());
                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                byProductSku.setBrandId(brandApiRepository.findByName(Supplier.SELLER_BRAND.getName()).get().getId());
                evaluateDescription(byProductSku, furniture2GoProduct);
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                setPriceAndQuantity(furniture2GoProduct, byProductSku);
                assignCategories(byProductSku, furniture2GoProduct.getDescription());
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            }
        }
        updateBigCommerceProducts(updatedBcProductDataList);
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
                    logger.info(request.getBody().getName());
                    result = restTemplate.postForObject(uri, request, BigCommerceApiProduct.class);
                    BcProductData resultData = result.getData();
                    resultData.set_id(product.get_id());
                    resultData = bigCommerceApiService.update(resultData);
                    updateImage(resultData, restTemplate);
                    logger.info("Successfully sent Maison Product to Big Commerce for the product id {}, and sku {}", resultData.getId(), resultData.getSku());
                } else {
                    String url = uri + "/" + product.getId();
                    ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, BigCommerceApiProduct.class);
                    BcProductData data = responseEntity.getBody().getData();
                    updateImage(data, restTemplate);
                    logger.info("Successfully updated the Maison Product on Big Commerce for the product id {}, and sku {}", data.getId(), data.getSku());
                }
            } catch (Exception duplicateRecordException) {
                logger.error("Duplicate record found with the name {}", request.getBody().getName());
                duplicateRecords.add(product);
                continue;
            }
        }
        if (!duplicateRecords.isEmpty()) {
            logger.info("Found duplicate products while processing Furniture2Go products. Processing the duplicate products by updating the name attribute");
            //processDuplicateRecords(duplicateRecords);
        }
        logger.info("Successfully Pushed Furniture2Go Products to Commerce");
    }

    private void setPriceAndQuantity(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        evaluatePrice(furniture2GoProduct, byProductSku);
        byProductSku.setInventoryLevel(furniture2GoProduct.getStockLevel() < 0 ? 0 : furniture2GoProduct.getStockLevel());
        byProductSku.setAvailability(BcConstants.PREORDER);
        if (furniture2GoProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
        }
    }

    private void evaluatePrice(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = furniture2GoProduct.getPrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
            byProductSku.setSalePrice(originalPrice.intValue());
            if (originalPrice.compareTo(new BigDecimal(higherLimitHDPrice)) > 0) {
                BigDecimal salePrice = originalPrice.add(DefineUtils.percentage(originalPrice, new BigDecimal(percentageLow)));
                byProductSku.setSalePrice(salePrice.intValue());
            }
        }
    }

    private void assignCategories(BcProductData data, String title) {
        List<Integer> categories = new ArrayList<>();

        for (Category category : Category.values()) {
            if (title.contains(category.getCategoryWord())) {
                categories.add(category.getCategoryCode());
            }
        }
        categories.add(Category.FURNITURE.getCategoryCode());
        data.setCategories(categories);
    }

    private void evaluateDescription(BcProductData byProductSku, Furniture2GoProduct furniture2GoProduct) {

        //Height logic
        String heightDelimeter = getDelimiter(furniture2GoProduct.getHeight());
        StringBuilder dimensionsDescription = new StringBuilder();
        if (!heightDelimeter.isEmpty()) {
            List<String> heightList = evaluateDimensions(heightDelimeter, furniture2GoProduct.getHeight());
            if (!heightList.isEmpty()) {
                dimensionsDescription.append("\nLower Height : " + heightList.get(0));
                dimensionsDescription.append("\nUpper Height : " + heightList.get(1));
                byProductSku.setHeight(Integer.parseInt(heightList.get(0)));
            }
        } else {
            dimensionsDescription.append("\nHeight : " + furniture2GoProduct.getHeight());
            byProductSku.setHeight(new BigDecimal(furniture2GoProduct.getHeight()).intValue());
        }

        //Width logic
        String widthDelimeter = getDelimiter(furniture2GoProduct.getWidth());
        if (!widthDelimeter.isEmpty()) {
            List<String> widthtList = evaluateDimensions(widthDelimeter, furniture2GoProduct.getWidth());
            if (!widthtList.isEmpty()) {
                dimensionsDescription.append("\nLower Width : " + widthtList.get(0));
                dimensionsDescription.append("\nUpper Width : " + widthtList.get(1));
                byProductSku.setWidth(Integer.parseInt(widthtList.get(0)));
            }
        } else {
            dimensionsDescription.append("\nWidth : " + furniture2GoProduct.getWidth());
            byProductSku.setHeight(new BigDecimal(furniture2GoProduct.getWidth()).intValue());
        }

        //Depth logic
        String depthDelimeter = getDelimiter(furniture2GoProduct.getDepth());
        if (!depthDelimeter.isEmpty()) {
            List<String> depthtList = evaluateDimensions(depthDelimeter, furniture2GoProduct.getDepth());
            if (!depthtList.isEmpty()) {
                dimensionsDescription.append("\nLower Depth : " + depthtList.get(0));
                dimensionsDescription.append("\nUpper Depth : " + depthtList.get(1));
                byProductSku.setDepth(Integer.parseInt(depthtList.get(0)));
            }
        } else {
            dimensionsDescription.append("\nDepth : " + furniture2GoProduct.getDepth());
            byProductSku.setDepth(new BigDecimal(furniture2GoProduct.getDepth()).intValue());
        }
        byProductSku.setDescription(furniture2GoProduct.getDescription() + "\n" + dimensionsDescription.toString());
    }




    private void updateImage(BcProductData data, RestTemplate restTemplate) throws Exception {
        Optional<Furniture2GoProduct> byProductSku = furniture2GoService.findByProductSku(data.getSku());
        if (byProductSku.isPresent()) {
            Furniture2GoProduct furniture2GoProduct = byProductSku.get();
            List<String> imagesList = getImagesList(furniture2GoProduct);

            URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT + "/" + data.getId() + "/images");
            BcProductImageData imageData;
            HttpEntity<BcProductImageData> request = null;
            boolean ifFirstImage = true;
            int sortOrder = 1;
            int imageDesriptionCount = 1;
            BigCommerceApiImage bigCommerceApiImage;
            List<String> filteredImagesList = imagesList.stream().filter(image -> StringUtils.isNotEmpty(image)).collect(Collectors.toList());
            for (String image : filteredImagesList) {
                try {
                    imageData = new BcProductImageData();
                    imageData.setId(data.getId());
                    imageData.setSortOrder(sortOrder++);
                    imageData.setIsThumbnail(ifFirstImage);
                    imageData.setDescription("Image_" + imageDesriptionCount++);
                    imageData.setImageUrl(refineImageUrl(image));
                    imageData.setImageFile(refineImageUrl(image));
                    request = new HttpEntity<>(imageData, bigCommerceApiService.getHttpHeaders());
                    bigCommerceApiImage = restTemplate.postForObject(uri, request, BigCommerceApiImage.class);
                    bigCommerceImageApiService.create(bigCommerceApiImage.getData());
                    ifFirstImage = false;
                } catch (Exception ex) {
                    logger.error("Exception occurred while processing images for the product id {}", data.getId());
                    continue;
                }
            }
        }
    }


    private String refineImageUrl(String imageUrl) {
        String result = imageUrl.replace("https", "http");
        if (imageUrl.contains("?")) {
            result = result.substring(0, result.indexOf("?"));
        }
        return result;
    }

    private List<String> getImagesList(Furniture2GoProduct furniture2GoProduct) {
        List<String> imagesUrls = new ArrayList<>();
        imagesUrls.add(furniture2GoProduct.getImageURL1());
        imagesUrls.add(furniture2GoProduct.getImageURL2());
        imagesUrls.add(furniture2GoProduct.getImageURL3());
        imagesUrls.add(furniture2GoProduct.getImageURL4());
        imagesUrls.add(furniture2GoProduct.getImageURL5());
        imagesUrls.add(furniture2GoProduct.getImageURL6());
        imagesUrls.add(furniture2GoProduct.getImageURL7());
        imagesUrls.add(furniture2GoProduct.getImageURL8());
        imagesUrls.add(furniture2GoProduct.getImageURL9());
        imagesUrls.add(furniture2GoProduct.getImageURL10());
        imagesUrls.add(furniture2GoProduct.getImageURL11());
        imagesUrls.add(furniture2GoProduct.getImageURL12());
        return imagesUrls;
    }

}
