package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiProduct;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceImageApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.artisan.service.ArtisanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
@Qualifier("artisanDataService")
public class GenerateBCArtisanDataServiceImpl implements GenerateBCDataService<BcProductData> {

    public static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";
    private final Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);
    @Autowired
    ArtisanService artisanService;
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

    @Override
    public void generateBcProductsFromSupplier(List<BcProductData> bcProductDataList) throws Exception {
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
     /*   for (BcProductData bcProductData : bcProductDataList)
        {

        } */
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
                    //updateImage(resultData, restTemplate);
                    logger.info("Successfully sent Artisan Product to Big Commerce for the product id {}, and sku {}", resultData.getId(), resultData.getSku());
                } else {
                    String url = uri + "/" + product.getId();
                    ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, BigCommerceApiProduct.class);
                    BcProductData data = responseEntity.getBody().getData();
                    logger.info("Successfully updated the Artisan Product on Big Commerce for the product id {}, and sku {}", data.getId(), data.getSku());
                }
            } catch (Exception duplicateRecordException) {
                logger.error("Duplicate record found with the name {}", request.getBody().getName());
                duplicateRecords.add(product);
                continue;
            }
        }
        if (!duplicateRecords.isEmpty()) {
            logger.info("Found duplicate products while processing maison products. Processing the duplicate products by updating the name attribute");
            //processDuplicateRecords(duplicateRecords);
        }
        logger.info("Successfully Updated Relavant Products");
    }


   /* private int evaluatePrice(MaisonProduct maisonProd) {
        BigDecimal decimalPrice = null;
        if (StringUtils.isEmpty(maisonProd.getMspPrice()) || "N/A".equals(maisonProd.getMspPrice())) {
            if (!StringUtils.isEmpty(maisonProd.getTradePrice())) {
                decimalPrice = new BigDecimal(maisonProd.getTradePrice());
                decimalPrice = decimalPrice.multiply(BigDecimal.valueOf(2));
            }
        } else {
            decimalPrice = new BigDecimal(maisonProd.getMspPrice());
        }
        decimalPrice = decimalPrice.add(new BigDecimal(1));
        return decimalPrice.intValue();

    }


    private void setDimensions(BigCommerceCsvProduct bigCommerceCsvProduct, String size) {
        StringTokenizer st = new StringTokenizer(size, "x");
        while (st.hasMoreTokens()) {
            String nextString = st.nextToken();
            if (nextString.contains("H")) {
                bigCommerceCsvProduct.setProductHeight(nextString.replaceAll("H", "") + "cm");
            } else if (nextString.contains("W")) {
                bigCommerceCsvProduct.setProductWidth(nextString.replaceAll("W", "") + "cm");
            } else if (nextString.contains("D")) {
                bigCommerceCsvProduct.setProductDepth(nextString.replaceAll("D", "") + "cm");
            }
        }
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
        ArtisanProduct maisonProduct = artisanService.findByProductSku(data.getSku());
        /*
        List<String> images = Arrays.asList(maisonProduct.getImages().split(","));
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT + "/" + data.getId() + "/images");
        BcProductImageData imageData;
        HttpEntity<BcProductImageData> request = null;
        boolean ifFirstImage = true;
        int sortOrder = 1;
        int imageDesriptionCount = 1;
        BigCommerceApiImage bigCommerceApiImage;
        List<String> filteredImagesList = images.stream().filter(image -> StringUtils.isNotEmpty(image)).collect(Collectors.toList());
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
        } */
   // }

    private void processDuplicateRecords(List<BcProductData> duplicateRecords) throws Exception {
        logger.info("Started processing duplicate records for Maison");
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = null;
        List<BcProductData> duplicateRecords1 = new ArrayList<>();
        BigCommerceApiProduct result = null;
    /*    for (BcProductData data : duplicateRecords) {
            logger.info("Started processing duplicate record for product sku {} and product name {}", data.getSku(), data.getName());
            MaisonProduct byProductSku = maisonService.findByProductSku(data.getSku());
            byProductSku.setTitle(byProductSku.getTitle() + " " + byProductSku.getProductCode());
            MaisonProduct updatedMaisonProduct = maisonService.update(byProductSku);
            data.setName(updatedMaisonProduct.getTitle());
            try {
                request = new HttpEntity<>(data, bigCommerceApiService.getHttpHeaders());
                logger.info(request.getBody().getName());
                result = restTemplate.postForObject(uri, request, BigCommerceApiProduct.class);
                BcProductData bcProductData = bigCommerceApiService.create(result.getData());
                updateImage(bcProductData, restTemplate);
            } catch (Exception exception) {
                logger.error("Duplicate record found with the name {}", request.getBody().getName());
                continue;
            }

        } */

        logger.info("Successfully finished processing the duplicate records for Maison");
    }

}