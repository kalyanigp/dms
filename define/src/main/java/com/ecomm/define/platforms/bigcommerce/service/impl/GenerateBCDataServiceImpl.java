package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.controller.BigCommerceProductApiController;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiImage;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiProduct;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceCsvProduct;
import com.ecomm.define.platforms.bigcommerce.ennum.Category;
import com.ecomm.define.platforms.bigcommerce.repository.BigcBrandApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceImageApiService;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.maison.domain.MaisonProduct;
import com.ecomm.define.suppliers.maison.service.MaisonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class GenerateBCDataServiceImpl implements GenerateBCDataService<MaisonProduct> {

    public static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";
    private final Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);
    @Autowired
    MaisonService maisonService;
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


    /*@Override
    public void generateBcData() {
        List<MaisonProduct> maisonProductList = maisonService.findAll();
        List<BigCommerceCsvProduct> bigCommerceCsvProductList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (MaisonProduct maisonProd : maisonProductList) {

            BigCommerceCsvProduct bigCommerceCsvProduct = modelMapper.map(maisonProd, BigCommerceCsvProduct.class);
            bigCommerceCsvProduct.setCategory("Furniture");
            bigCommerceCsvProduct.setAllowPurchases("N");
            bigCommerceCsvProduct.setBrandName("Define");
            if (maisonProd.getStockQuantity() > 0) {
                bigCommerceCsvProduct.setAllowPurchases("Y");
            }
            bigCommerceCsvProduct.setTitle("Define " + bigCommerceCsvProduct.getTitle());
            bigCommerceCsvProduct.setMspPrice(maisonProd.getMspPrice());
            bigCommerceCsvProduct.setTradePrice(maisonProd.getTradePrice());
            bigCommerceCsvProduct.setProductWeight("0");
            bigCommerceCsvProduct.setFixedShippingCost("");
            bigCommerceCsvProduct.setTrackInventory("by product");
            bigCommerceCsvProduct.setProductType("P");
            if (maisonProd.getPackingSpec() != null) {
                int index = 0;
                if (maisonProd.getPackingSpec().contains("Kg")) {
                    index = maisonProd.getPackingSpec().indexOf("Kg");
                } else if (maisonProd.getPackingSpec().contains("KG")) {
                    index = maisonProd.getPackingSpec().indexOf("KG");
                }
                if (index > 0) {
                    bigCommerceCsvProduct.setProductWeight(maisonProd.getPackingSpec().substring(index - 3, index));
                }
                String productWeight = bigCommerceCsvProduct.getProductWeight();
                if (productWeight != null) {
                    if (productWeight.contains("Weight")) {
                        bigCommerceCsvProduct.setProductWeight(productWeight.replaceAll("Weight", ""));
                    } else if (productWeight.contains("WEIGHT")) {
                        bigCommerceCsvProduct.setProductWeight(productWeight.replaceAll("WEIGHT", ""));
                    }
                }
            }
            if (maisonProd.getMaterial() != null) {
                bigCommerceCsvProduct.setProductDescription(maisonProd.getMaterial().replaceAll(",", ""));
            }
            bigCommerceCsvProduct.setProductDescription(bigCommerceCsvProduct.getProductDescription() + " " + maisonProd.getSize() + " " + maisonProd.getPackingSpec());

            bigCommerceCsvProductList.add(bigCommerceCsvProduct);
            if (maisonProd.getImages() != null && !maisonProd.getImages().isEmpty()) {
                StringTokenizer st = new StringTokenizer(maisonProd.getImages(), ",");

                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_1(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_1("0");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_1("Y");
                    bigCommerceCsvProduct.setProductImageDescription_1(bigCommerceCsvProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_2(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_2("1");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_2("N");
                    bigCommerceCsvProduct.setProductImageDescription_2(bigCommerceCsvProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_3(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_3("2");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_3("N");
                    bigCommerceCsvProduct.setProductImageDescription_3(bigCommerceCsvProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_4(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_4("3");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_4("N");
                    bigCommerceCsvProduct.setProductImageDescription_4(bigCommerceCsvProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_5(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_5("4");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_5("N");
                    bigCommerceCsvProduct.setProductImageDescription_5(bigCommerceCsvProduct.getTitle());
                }

                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_6(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_6("5");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_6("N");
                    bigCommerceCsvProduct.setProductImageDescription_6(bigCommerceCsvProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_7(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_7("6");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_7("N");
                    bigCommerceCsvProduct.setProductImageDescription_7(bigCommerceCsvProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_8(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_8("7");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_8("N");
                    bigCommerceCsvProduct.setProductImageDescription_8(bigCommerceCsvProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceCsvProduct.setProductImageFile_9(fileURL);
                    bigCommerceCsvProduct.setProductImageSort_9("8");
                    bigCommerceCsvProduct.setProductImageIsThumbnail_9("N");
                    bigCommerceCsvProduct.setProductImageDescription_9(bigCommerceCsvProduct.getTitle());
                }
            }
            bigCommerceCsvProduct.setProductCondition("New");
            bigCommerceCsvProduct.setShowProductCondition("Y");
            bigCommerceCsvProduct.setStockQuantity(String.valueOf(maisonProd.getStockQuantity()));
            bigCommerceCsvProduct.setProductAvailability(getProductAvailability(Double.parseDouble(bigCommerceCsvProduct.getTradePrice()), maisonProd.getStockQuantity()));
            setDimensions(bigCommerceCsvProduct, maisonProd.getSize());
        }
        bigCommerceService.saveAll(bigCommerceCsvProductList);
    }*/

    @Override
    public void generateBcProductsFromSupplier(List<MaisonProduct> updatedMaisonProductList) throws Exception {
        List<BcProductData> updatedBcProductDataList = new ArrayList<>();
        for (MaisonProduct maisonProd : updatedMaisonProductList) {
            BcProductData byProductSku = bigCommerceApiService.findByProductSku(maisonProd.getProductCode());

            if (byProductSku == null) {
                byProductSku = new BcProductData();
                setPriceAndQuantity(maisonProd, byProductSku);
                assignCategories(byProductSku, maisonProd.getTitle());
                byProductSku.setSku(maisonProd.getProductCode());
                byProductSku.setName(Supplier.SELLER_BRAND.getName() + " " + maisonProd.getTitle());

                byProductSku.setSupplier(Supplier.MAISON.getName());
                byProductSku.setType(BcConstants.TYPE);
                byProductSku.setWeight(20);
                byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
                byProductSku.setAvailability(BcConstants.PREORDER);
                if(maisonProd.getStockQuantity() > 0) {
                    byProductSku.setAvailability(BcConstants.AVAILABLE);
                }

                byProductSku.setBrandId(brandApiRepository.findByName(Supplier.SELLER_BRAND.getName()).get().getId());
                if (maisonProd.getPackingSpec() != null) {
                    int index = 0;
                    if (maisonProd.getPackingSpec().contains("Kg")) {
                        index = maisonProd.getPackingSpec().indexOf("Kg");
                    } else if (maisonProd.getPackingSpec().contains("KG")) {
                        index = maisonProd.getPackingSpec().indexOf("KG");
                    }
                    if (index > 0) {
                        String weight = maisonProd.getPackingSpec().substring(index - 3, index);
                        if (weight != null){
                            weight = weight.replaceAll(" ","").replaceAll(":","");
                            double dWeight = Double.parseDouble(weight);
                            if ((dWeight == Math.ceil(dWeight)) && !Double.isInfinite(dWeight)) {
                                byProductSku.setWeight((int)dWeight);
                            }
                        }

                    }
                }
                if (maisonProd.getMaterial() != null) {
                    byProductSku.setDescription(maisonProd.getMaterial().replaceAll(",", ""));
                }
                byProductSku.setDescription(byProductSku.getDescription() + " " + maisonProd.getSize() + " " + maisonProd.getPackingSpec());

                BcProductData bcProductData = bigCommerceApiService.create(byProductSku);

                updatedBcProductDataList.add(bcProductData);
            } else {
                setPriceAndQuantity(maisonProd, byProductSku);
                assignCategories(byProductSku, maisonProd.getTitle());
                BcProductData bcProductData = bigCommerceApiService.update(byProductSku);

                updatedBcProductDataList.add(bcProductData);
            }
        }
        updateBigCommerceProducts(updatedBcProductDataList);
    }

    private void setPriceAndQuantity(MaisonProduct maisonProd, BcProductData byProductSku) {
        int priceIntValue = evaluatePrice(maisonProd);
        byProductSku.setPrice(priceIntValue);
        byProductSku.setSalePrice(priceIntValue);
        byProductSku.setInventoryLevel(maisonProd.getStockQuantity() < 0 ? 0 : maisonProd.getStockQuantity());
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
                    logger.info("Successfully created the Maison Product to Big Commerce for the product id {}, and sku {}", resultData.getId(), resultData.getSku());
                } else {
                    String url = uri + "/" + product.getId();
                    ResponseEntity<BigCommerceApiProduct> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, BigCommerceApiProduct.class);
                    BcProductData data = responseEntity.getBody().getData();
                    logger.info("Successfully updated the Maison Product to Big Commerce for the product id {}, and sku {}", data.getId(), data.getSku());
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
        MaisonProduct maisonProduct = maisonService.findByProductSku(data.getSku());
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
        }
    }


    private void processDuplicateRecords(List<BcProductData> duplicateRecords) throws Exception {
        logger.info("Started processing duplicate records for Maison");
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = null;
        List<BcProductData> duplicateRecords1 = new ArrayList<>();
        BigCommerceApiProduct result = null;
        for (BcProductData data : duplicateRecords) {
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

        }

        logger.info("Successfully finished processing the duplicate records for Maison");
    }

}