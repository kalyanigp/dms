package com.ecomm.define.platforms.bigcommerce.service;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface GenerateBCDataService<T> {
    void generateBcProductsFromSupplier(List<T> productList) throws Exception;
}
