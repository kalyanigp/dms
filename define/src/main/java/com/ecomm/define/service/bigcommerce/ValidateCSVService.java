package com.ecomm.define.service.bigcommerce;

import com.ecomm.define.domain.BigCommerceProduct;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 21/04/2020.
 */
public interface ValidateCSVService {
   List<BigCommerceProduct> validate(List<BigCommerceProduct> bigCommerceProductList);
}