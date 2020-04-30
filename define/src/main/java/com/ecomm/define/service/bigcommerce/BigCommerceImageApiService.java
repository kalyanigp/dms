package com.ecomm.define.service.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.domain.bigcommerce.BcProductImageData;
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
