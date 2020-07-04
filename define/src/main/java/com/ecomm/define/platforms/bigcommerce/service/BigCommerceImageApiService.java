package com.ecomm.define.platforms.bigcommerce.service;

import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface BigCommerceImageApiService {
    BcProductImageData create(final BcProductImageData bcProduct);

    BcProductImageData findBy_Id(final ObjectId id);

    Optional<BcProductImageData> findById(final Integer id);

    List<BcProductImageData> findAll();

    BcProductImageData update(BcProductImageData bcProduct);

    void saveAll(List<BcProductImageData> bcProductList);

    void delete(BcProductImageData id);
}
