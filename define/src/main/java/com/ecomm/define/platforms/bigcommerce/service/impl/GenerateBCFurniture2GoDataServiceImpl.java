package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageDataList;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                byProductSku = setPriceAndQuantity(furniture2GoProduct, byProductSku);
                byProductSku = assignCategories(byProductSku, furniture2GoProduct.getDescription());
                byProductSku.setSku(furniture2GoProduct.getSku());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + furniture2GoProduct.getProductName()+" "+furniture2GoProduct.getFinish());

                byProductSku.setSupplier(Supplier.FURNITURE2GO.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(furniture2GoProduct.getWeight().intValue());
                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                byProductSku.setBrandId(brandApiRepository.findByName(Supplier.SELLER_BRAND.getName()).get().getId());
                byProductSku = evaluateDescription(byProductSku, furniture2GoProduct);
                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);
                updatedBcProductDataList.add(bcProductData);
            } else {
                byProductSku = setPriceAndQuantity(furniture2GoProduct, byProductSku);
                byProductSku = assignCategories(byProductSku, furniture2GoProduct.getDescription());
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
                    logger.info("Successfully sent Furniture2Go Product to Big Commerce for the product id {}, and sku {}", resultData.getId(), resultData.getSku());
                } else {
                    String url = uri + "/" + product.getId();
                    ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, BigCommerceApiProduct.class);
                    BcProductData data = responseEntity.getBody().getData();
                    updateImage(data, restTemplate);
                    logger.info("Successfully updated the Furniture2Go Product on Big Commerce for the product id {}, and sku {}", data.getId(), data.getSku());
                }
            } catch (Exception ex) {
                logger.error("Error while updating the Furniture2Go inventory", request.getBody().getName());
                duplicateRecords.add(product);
                continue;
            }
        }
        if (!duplicateRecords.isEmpty()) {
            logger.info("Found duplicate products while processing Furniture2Go products. Processing the duplicate products by updating the name attribute");
        }
        logger.info("Successfully Pushed Furniture2Go Products to Commerce");
    }

    private BcProductData setPriceAndQuantity(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        byProductSku = evaluatePrice(furniture2GoProduct, byProductSku);
        byProductSku.setInventoryLevel(furniture2GoProduct.getStockLevel() < 0 ? 0 : furniture2GoProduct.getStockLevel());
        byProductSku.setAvailability(BcConstants.PREORDER);
        if (furniture2GoProduct.getStockLevel() > 0) {
            byProductSku.setAvailability(BcConstants.AVAILABLE);
        }
        return byProductSku;
    }

    private BcProductData evaluatePrice(Furniture2GoProduct furniture2GoProduct, BcProductData byProductSku) {
        BigDecimal originalPrice = furniture2GoProduct.getPrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
            byProductSku.setPrice(originalPrice.intValue());
            if (originalPrice.compareTo(new BigDecimal(higherLimitHDPrice)) > 0) {
                BigDecimal retailPrice = originalPrice.add(DefineUtils.percentage(originalPrice, new BigDecimal(percentageLow)));
                byProductSku.setRetailPrice(retailPrice.intValue());
            }
        }
        return byProductSku;
    }

    private BcProductData assignCategories(BcProductData data, String title) {
        Set<Integer> categories = new HashSet<>();
        categories.add(Category.FURNITURE.getCategoryCode());
        for (Category category : Category.values()) {
            if (title.toLowerCase().contains(category.getCategoryWord().toLowerCase())) {
                categories.add(category.getCategoryCode());
            }
        }
        data.setCategories(categories.parallelStream().collect(Collectors.toList()));
        return data;
    }

    private BcProductData evaluateDescription(BcProductData byProductSku, Furniture2GoProduct furniture2GoProduct) {
        //Height logic
        StringBuilder dimensionsDescription = new StringBuilder();
        String heightDelimeter = getDelimiter(furniture2GoProduct.getHeight());
        dimensionsDescription.append("Height : " + furniture2GoProduct.getHeight() + " mm");
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
        dimensionsDescription.append("Width : " + furniture2GoProduct.getWidth() + " mm");
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
        dimensionsDescription.append("Depth : " + furniture2GoProduct.getDepth() + " mm");
        if (!depthDelimeter.isEmpty()) {
            List<String> depthList = evaluateDimensions(widthDelimeter, furniture2GoProduct.getDepth());
            if (!depthList.isEmpty()) {
                byProductSku.setDepth(Integer.parseInt(depthList.get(0)));
            }
        } else {
            byProductSku.setDepth(new BigDecimal(furniture2GoProduct.getDepth()).intValue());
        }
        byProductSku.setDescription(furniture2GoProduct.getDescription() + "\n" + dimensionsDescription.toString());
        return byProductSku;
    }

    private void updateImage(BcProductData data, RestTemplate restTemplate) throws Exception {
        Optional<Furniture2GoProduct> byProductSku = furniture2GoService.findByProductSku(data.getSku());
        if (byProductSku.isPresent()) {
            Furniture2GoProduct furniture2GoProduct = byProductSku.get();
            List<String> imagesList = furniture2GoProduct.getImages();

            URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT + "/" + data.getId() + "/images");
            if (checkImagesNotExists(uri, restTemplate)) {
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
                        imageData.setImageUrl(image);
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
    }

    private boolean checkImagesNotExists(URI uri, RestTemplate restTemplate) {
        ResponseEntity<BcProductImageDataList> responseEntity = null;
        HttpEntity<BcProductImageDataList> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BcProductImageDataList.class);
        List<BcProductImageData> bcCategoryDataList = responseEntity.getBody().getData();
        return bcCategoryDataList.isEmpty();
    }

}
