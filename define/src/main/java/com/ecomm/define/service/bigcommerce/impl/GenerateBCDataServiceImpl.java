package com.ecomm.define.service.bigcommerce.impl;

import com.ecomm.define.domain.BigCommerceProduct;
import com.ecomm.define.domain.supplier.maison.MaisonProduct;
import com.ecomm.define.service.bigcommerce.BigCommerceService;
import com.ecomm.define.service.bigcommerce.GenerateBCDataService;
import com.ecomm.define.service.supplier.maison.MaisonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<BigCommerceProduct> bigCommerceProductList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (MaisonProduct maisonProd : maisonProductList) {

            BigCommerceProduct bigCommerceProduct = modelMapper.map(maisonProd, BigCommerceProduct.class);
            bigCommerceProduct.setCategory("Furniture");
            bigCommerceProduct.setAllowPurchases("N");
            bigCommerceProduct.setBrandName("Define");
            if (maisonProd.getStockQuantity() > 0) {
                bigCommerceProduct.setAllowPurchases("Y");
            }
            bigCommerceProduct.setTitle("Define " + bigCommerceProduct.getTitle());
            bigCommerceProduct.setMspPrice(maisonProd.getMspPrice());
            bigCommerceProduct.setTradePrice(maisonProd.getTradePrice());
            bigCommerceProduct.setProductWeight("0");
            bigCommerceProduct.setFixedShippingCost("");
            bigCommerceProduct.setTrackInventory("by product");
            bigCommerceProduct.setProductType("P");
            if (maisonProd.getPackingSpec() != null)
            {
                int index = 0;
                if (maisonProd.getPackingSpec().contains("Kg")){
                     index = maisonProd.getPackingSpec().indexOf("Kg");
                } else if (maisonProd.getPackingSpec().contains("KG")){
                    index = maisonProd.getPackingSpec().indexOf("KG");
                }
                if (index>0) {
                    bigCommerceProduct.setProductWeight(maisonProd.getPackingSpec().substring(index - 3, index));
                }
                String productWeight = bigCommerceProduct.getProductWeight();
                if (productWeight != null) {
                    if (productWeight.contains("Weight")) {
                        bigCommerceProduct.setProductWeight(productWeight.replaceAll("Weight", ""));
                    } else if (productWeight.contains("WEIGHT")) {
                        bigCommerceProduct.setProductWeight(productWeight.replaceAll("WEIGHT", ""));
                    }
                }
            }
            if (maisonProd.getMaterial() != null) {
                bigCommerceProduct.setProductDescription(maisonProd.getMaterial().replaceAll(",", ""));
            }
            bigCommerceProduct.setProductDescription(bigCommerceProduct.getProductDescription() + " " + maisonProd.getSize() + " " + maisonProd.getPackingSpec());

            bigCommerceProductList.add(bigCommerceProduct);
            if (maisonProd.getImages() != null && !maisonProd.getImages().isEmpty()) {
                StringTokenizer st = new StringTokenizer(maisonProd.getImages(), ",");

                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_1(fileURL);
                    bigCommerceProduct.setProductImageSort_1("0");
                    bigCommerceProduct.setProductImageIsThumbnail_1("Y");
                    bigCommerceProduct.setProductImageDescription_1(bigCommerceProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_2(fileURL);
                    bigCommerceProduct.setProductImageSort_2("1");
                    bigCommerceProduct.setProductImageIsThumbnail_2("N");
                    bigCommerceProduct.setProductImageDescription_2(bigCommerceProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_3(fileURL);
                    bigCommerceProduct.setProductImageSort_3("2");
                    bigCommerceProduct.setProductImageIsThumbnail_3("N");
                    bigCommerceProduct.setProductImageDescription_3(bigCommerceProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_4(fileURL);
                    bigCommerceProduct.setProductImageSort_4("3");
                    bigCommerceProduct.setProductImageIsThumbnail_4("N");
                    bigCommerceProduct.setProductImageDescription_4(bigCommerceProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_5(fileURL);
                    bigCommerceProduct.setProductImageSort_5("4");
                    bigCommerceProduct.setProductImageIsThumbnail_5("N");
                    bigCommerceProduct.setProductImageDescription_5(bigCommerceProduct.getTitle());
                }

                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_6(fileURL);
                    bigCommerceProduct.setProductImageSort_6("5");
                    bigCommerceProduct.setProductImageIsThumbnail_6("N");
                    bigCommerceProduct.setProductImageDescription_6(bigCommerceProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_7(fileURL);
                    bigCommerceProduct.setProductImageSort_7("6");
                    bigCommerceProduct.setProductImageIsThumbnail_7("N");
                    bigCommerceProduct.setProductImageDescription_7(bigCommerceProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_8(fileURL);
                    bigCommerceProduct.setProductImageSort_8("7");
                    bigCommerceProduct.setProductImageIsThumbnail_8("N");
                    bigCommerceProduct.setProductImageDescription_8(bigCommerceProduct.getTitle());
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductImageFile_9(fileURL);
                    bigCommerceProduct.setProductImageSort_9("8");
                    bigCommerceProduct.setProductImageIsThumbnail_9("N");
                    bigCommerceProduct.setProductImageDescription_9(bigCommerceProduct.getTitle());
                }
            }
            bigCommerceProduct.setProductCondition("New");
            bigCommerceProduct.setShowProductCondition("Y");
            bigCommerceProduct.setStockQuantity(String.valueOf(maisonProd.getStockQuantity()));
            bigCommerceProduct.setProductAvailability(getProductAvailability(Double.parseDouble(bigCommerceProduct.getTradePrice()), maisonProd.getStockQuantity()));
            setDimensions(bigCommerceProduct, maisonProd.getSize());
        }
        bigCommerceService.saveAll(bigCommerceProductList);
    }

    private void setDimensions(BigCommerceProduct bigCommerceProduct, String size) {
        StringTokenizer st = new StringTokenizer(size, "x");
        while (st.hasMoreTokens()) {
            String nextString = st.nextToken();
            if (nextString.contains("H")) {
                bigCommerceProduct.setProductHeight(nextString.replaceAll("H", "") + "cm");
            } else if (nextString.contains("W")) {
                bigCommerceProduct.setProductWidth(nextString.replaceAll("W", "") + "cm");
            } else if (nextString.contains("D")) {
                bigCommerceProduct.setProductDepth(nextString.replaceAll("D", "") + "cm");
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