package com.ecomm.define.service.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BcProductData;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface BigCommerceApiService {
    BcProductData create(final BcProductData bcProduct);

    BcProductData findBy_Id(final ObjectId id);

    Optional<BcProductData> findById(final String id);

    BcProductData findByProductSku(final String sku);

    List<BcProductData> findAll();

    BcProductData update(BcProductData bcProduct);

    void saveAll(List<BcProductData> bcProductList);

    void delete(final ObjectId id);

    List<BcProductData> findBySupplier(String supplier);
}