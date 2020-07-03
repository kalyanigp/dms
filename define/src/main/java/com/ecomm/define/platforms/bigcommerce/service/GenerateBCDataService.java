package com.ecomm.define.platforms.bigcommerce.service;

import com.ecomm.define.suppliers.maison.domain.MaisonProduct;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface GenerateBCDataService {
    //void generateBcData();

    void generateBcProductsFromMaison(List<MaisonProduct> maisonProductList) throws Exception;
}
