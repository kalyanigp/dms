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
            bigCommerceProduct.setProductWeight("100");
            bigCommerceProduct.setTrackInventory("by product");
            bigCommerceProduct.setProductType("P");
            bigCommerceProductsList.add(bigCommerceProduct);
        }

        bcRepository.saveAll(bigCommerceProductsList);


    }
}
