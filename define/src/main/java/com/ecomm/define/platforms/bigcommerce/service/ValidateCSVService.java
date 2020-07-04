package com.ecomm.define.platforms.bigcommerce.service;

import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceCsvProduct;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 21/04/2020.
 */
public interface ValidateCSVService {
    List<BigCommerceCsvProduct> validate(List<BigCommerceCsvProduct> bigCommerceCsvProductList);
}
