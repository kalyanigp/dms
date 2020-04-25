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
            if (maisonProd.getStockQuantity() > 0) {
                bigCommerceProduct.setAllowPurchases("Y");
            }
            bigCommerceProduct.setTitle("Define "+bigCommerceProduct.getTitle());
            bigCommerceProduct.setMspPrice(maisonProd.getMspPrice());
            bigCommerceProduct.setTradePrice(maisonProd.getTradePrice());
            bigCommerceProduct.setProductWeight("0");
            bigCommerceProduct.setFixedShippingCost("");
            bigCommerceProduct.setTrackInventory("by product");
            bigCommerceProduct.setProductType("P");
            bigCommerceProduct.setProductDescription(maisonProd.getMaterial().replaceAll(",", ""));
            bigCommerceProductList.add(bigCommerceProduct);
            if (maisonProd.getImages() != null && !maisonProd.getImages().isEmpty()) {
                StringTokenizer st = new StringTokenizer(maisonProd.getImages(), ",");

                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductFile_1(fileURL);
                    bigCommerceProduct.setProductImageSort_1("1");
                    bigCommerceProduct.setProductImageIsThumbnail_1("Y");
                    bigCommerceProduct.setProductImage_1(fileURL);
                    bigCommerceProduct.setProductImageDescription_1(fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length()));
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductFile_2(fileURL);
                    bigCommerceProduct.setProductImageSort_2("2");
                    bigCommerceProduct.setProductImageIsThumbnail_2("N");
                    bigCommerceProduct.setProductImage_2(fileURL);
                    bigCommerceProduct.setProductImageDescription_2(fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length()));
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductFile_3(fileURL);
                    bigCommerceProduct.setProductImageSort_3("3");
                    bigCommerceProduct.setProductImageIsThumbnail_3("N");
                    bigCommerceProduct.setProductImage_3(fileURL);
                    bigCommerceProduct.setProductImageDescription_3(fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length()));
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductFile_4(fileURL);
                    bigCommerceProduct.setProductImageSort_4("4");
                    bigCommerceProduct.setProductImageIsThumbnail_4("N");
                    bigCommerceProduct.setProductImage_4(fileURL);
                    bigCommerceProduct.setProductImageDescription_4(fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length()));
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductFile_5(fileURL);
                    bigCommerceProduct.setProductImageSort_5("5'");
                    bigCommerceProduct.setProductImageIsThumbnail_5("N");
                    bigCommerceProduct.setProductImage_5(fileURL);
                    bigCommerceProduct.setProductImageDescription_5(fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length()));
                }

                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductFile_6(fileURL);
                    bigCommerceProduct.setProductImageSort_6("6'");
                    bigCommerceProduct.setProductImageIsThumbnail_6("N");
                    bigCommerceProduct.setProductImage_6(fileURL);
                    bigCommerceProduct.setProductImageDescription_6(fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length()));
                }
                if (st.hasMoreTokens()) {
                    String fileURL = st.nextToken();
                    bigCommerceProduct.setProductFile_7(fileURL);
                    bigCommerceProduct.setProductImageSort_7("7'");
                    bigCommerceProduct.setProductImageIsThumbnail_7("N");
                    bigCommerceProduct.setProductImage_7(fileURL);
                    bigCommerceProduct.setProductImageDescription_7(fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length()));
                }
            }
            bigCommerceProduct.setCurrentStockLevel(String.valueOf(maisonProd.getStockQuantity()));
            bigCommerceProduct.setProductAvailability(getProductAvailability(Double.parseDouble(bigCommerceProduct.getTradePrice()), maisonProd.getStockQuantity()));
        }
        bigCommerceService.saveAll(bigCommerceProductList);
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