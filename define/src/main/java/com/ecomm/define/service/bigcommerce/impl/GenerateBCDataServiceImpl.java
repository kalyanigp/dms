package com.ecomm.define.service.bigcommerce.impl;

import com.ecomm.define.domain.bigcommerce.BigCommerceApiProduct;
import com.ecomm.define.domain.bigcommerce.BigCommerceCsvProduct;
import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.domain.supplier.maison.MaisonProduct;
import com.ecomm.define.service.bigcommerce.BigCommerceService;
import com.ecomm.define.service.bigcommerce.GenerateBCDataService;
import com.ecomm.define.service.supplier.maison.MaisonService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class GenerateBCDataServiceImpl implements GenerateBCDataService {

    @Autowired
    MaisonService maisonService;

    @Autowired
    BigCommerceService bigCommerceService;

    @Override
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
            if (maisonProd.getPackingSpec() != null)
            {
                int index = 0;
                if (maisonProd.getPackingSpec().contains("Kg")){
                     index = maisonProd.getPackingSpec().indexOf("Kg");
                } else if (maisonProd.getPackingSpec().contains("KG")){
                    index = maisonProd.getPackingSpec().indexOf("KG");
                }
                if (index>0) {
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
    }



    @Override
    public List<BigCommerceApiProduct> generateBcProductsFromMaison() {
        List<MaisonProduct> maisonProductList = maisonService.findAll();
        List<BigCommerceApiProduct> bigCommerceApiProductList = new ArrayList<>();
        BigCommerceApiProduct bigCommerceApiProduct = null;
        for (MaisonProduct maisonProd : maisonProductList) {
            bigCommerceApiProduct = new BigCommerceApiProduct();
            BcProductData data = bigCommerceApiProduct.getData();
            if(data == null) {
                data = new BcProductData();
            }
            data.setName(maisonProd.getTitle());
            data.setSku(maisonProd.getProductCode());

            BigDecimal decimalPrice = null;
            if(StringUtils.isEmpty(maisonProd.getMspPrice()) || "N/A".equals(maisonProd.getMspPrice())) {
                if (!StringUtils.isEmpty(maisonProd.getTradePrice())) {
                    decimalPrice = new BigDecimal(maisonProd.getTradePrice());
                    decimalPrice = decimalPrice.multiply(BigDecimal.valueOf(2));
                }
            } else {
                decimalPrice = new BigDecimal(maisonProd.getMspPrice());
            }
            decimalPrice = decimalPrice.add(new BigDecimal(1));
            int priceIntValue = decimalPrice.intValue();
            data.setPrice(priceIntValue);

            List<Integer> categories = new ArrayList<>();
            categories.add(69);
            data.setCategories(categories);
            data.setType("physical");
            data.setName("Define " + data.getName());
            data.setSalePrice(priceIntValue);
            data.setWeight(20);
            data.setInventoryLevel(maisonProd.getStockQuantity() < 0? 0 : maisonProd.getStockQuantity());
            data.setInventoryTracking("product");
            bigCommerceApiProduct.setData(data);
            bigCommerceApiProductList.add(bigCommerceApiProduct);
        }
        return bigCommerceApiProductList;
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
}