package com.ecomm.define.platforms.bigcommerce.service;

import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceCsvProduct;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface BigCommerceService {

    BigCommerceCsvProduct create(final BigCommerceCsvProduct bcProduct);
    BigCommerceCsvProduct findBy_Id(final ObjectId id);
    Optional<BigCommerceCsvProduct> findById(final String id);
    BigCommerceCsvProduct findByProductSku(final String sku);
    List<BigCommerceCsvProduct> findAll();
    BigCommerceCsvProduct update(BigCommerceCsvProduct bcProduct);
    void saveAll(List<BigCommerceCsvProduct> bcProductList);
    void delete(final ObjectId id);

}
