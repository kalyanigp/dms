package com.ecomm.define.service.bigcommerce;

import com.ecomm.define.domain.supplier.maison.MaisonProduct;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface GenerateBCDataService {
    //void generateBcData();

    void generateBcProductsFromMaison(List<MaisonProduct> maisonProductList) throws Exception;
}
