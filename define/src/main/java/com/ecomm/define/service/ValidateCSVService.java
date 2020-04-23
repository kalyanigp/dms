package com.ecomm.define.service;

import com.ecomm.define.domain.BigCommerceProduct;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 21/04/2020.
 */
public interface ValidateCSVService {
   void validate(List<BigCommerceProduct> bigCommerceProductList);
}
