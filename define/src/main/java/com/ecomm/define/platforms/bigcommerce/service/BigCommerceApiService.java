package com.ecomm.define.platforms.bigcommerce.service;

import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;

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

    HttpHeaders getHttpHeaders();

    String getStoreHash();

    String getBaseUrl();
}
