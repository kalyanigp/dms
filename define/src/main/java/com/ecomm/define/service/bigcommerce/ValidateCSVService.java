package com.ecomm.define.service.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BigCommerceCsvProduct;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 21/04/2020.
 */
public interface ValidateCSVService {
   List<BigCommerceCsvProduct> validate(List<BigCommerceCsvProduct> bigCommerceCsvProductList);
}
