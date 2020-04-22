package com.ecomm.define.service.impl;

import com.ecomm.define.domain.BigCommerceProducts;
import com.ecomm.define.domain.MaisonProducts;
import com.ecomm.define.repository.BigCommerceProductRepository;
import com.ecomm.define.repository.MaisonProductRepository;
import com.ecomm.define.service.GenerateBCDataService;
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
    MaisonProductRepository maisonRepository;

    @Autowired
    BigCommerceProductRepository bcRepository;

    @Override
    public void generateBcData() {
        List<MaisonProducts> maisonProductList = maisonRepository.findAll();
        List<BigCommerceProducts> bigCommerceProductsList = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for(MaisonProducts maisonProd : maisonProductList) {

            BigCommerceProducts bigCommerceProduct = modelMapper.map(maisonProd, BigCommerceProducts.class);
            bigCommerceProduct.setCategory("Furniture");
            bigCommerceProduct.setAllowPurchases("N");
            if(maisonProd.getStockQuantity() > 0) {
                bigCommerceProduct.setAllowPurchases("Y");
            }

            bigCommerceProduct.setMspPrice(maisonProd.getMspPrice());
            bigCommerceProduct.setTradePrice(maisonProd.getTradePrice());
            bigCommerceProduct.setProductWeight("0");
            bigCommerceProduct.setFixedShippingCost("");
            bigCommerceProduct.setTrackInventory("by product");
            bigCommerceProduct.setProductType("P");
            bigCommerceProduct.setProductDescription(maisonProd.getMaterial().replaceAll(",",""));
            bigCommerceProductsList.add(bigCommerceProduct);
            if(maisonProd.getImages() != null  && !maisonProd.getImages().isEmpty()){
                StringTokenizer st = new StringTokenizer(maisonProd.getImages(),",");
                if(st.hasMoreTokens()) {
                    bigCommerceProduct.setProductImageFile_1(st.nextToken());
                    bigCommerceProduct.setProductImageSort_1("1");
                    bigCommerceProduct.setProductImageIsThumbnail_1("Y");
                }
                if(st.hasMoreTokens()) {
                    bigCommerceProduct.setProductImageFile_2(st.nextToken());
                    bigCommerceProduct.setProductImageSort_2("2");
                    bigCommerceProduct.setProductImageIsThumbnail_2("N");
                }
                if(st.hasMoreTokens()) {
                    bigCommerceProduct.setProductImageFile_3(st.nextToken());
                    bigCommerceProduct.setProductImageSort_3("3");
                    bigCommerceProduct.setProductImageIsThumbnail_3("N");

                }
                if(st.hasMoreTokens()) {
                    bigCommerceProduct.setProductImageFile_4(st.nextToken());
                    bigCommerceProduct.setProductImageSort_4("4");
                    bigCommerceProduct.setProductImageIsThumbnail_4("N");

                }
                if(st.hasMoreTokens()) {
                    bigCommerceProduct.setProductImageFile_5(st.nextToken());
                    bigCommerceProduct.setProductImageSort_5("5'");
                    bigCommerceProduct.setProductImageIsThumbnail_5("N");

                }
                if(st.hasMoreTokens()) {
                    bigCommerceProduct.setProductImageFile_6(st.nextToken());
                    bigCommerceProduct.setProductImageSort_6("6'");
                    bigCommerceProduct.setProductImageIsThumbnail_6("N");

                }
                if(st.hasMoreTokens()) {
                    bigCommerceProduct.setProductImageFile_7(st.nextToken());
                    bigCommerceProduct.setProductImageSort_7("7'");
                    bigCommerceProduct.setProductImageIsThumbnail_7("N");

                }
            }
        }

        bcRepository.saveAll(bigCommerceProductsList);
    }
}
