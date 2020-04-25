package com.ecomm.define.service.bigcommerce;

import com.ecomm.define.domain.BigCommerceProduct;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface BigCommerceService {

    BigCommerceProduct create(final BigCommerceProduct bcProduct);
    BigCommerceProduct findBy_Id(final ObjectId id);
    Optional<BigCommerceProduct> findById(final String id);
    BigCommerceProduct findByProductSku(final String sku);
    List<BigCommerceProduct> findAll();
    BigCommerceProduct update(BigCommerceProduct bcProduct);
    void saveAll(List<BigCommerceProduct> bcProductList);
    void delete(final ObjectId id);

}
