package com.ecomm.define.repository.bigcommerce;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.domain.bigcommerce.BcProductImageData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BigcDataApiRepository extends MongoRepository<BcProductData, String> {
    BcProductData findBy_id(ObjectId _id);

    @Override
    Optional<BcProductData> findById(String s);

    @Query("{sku : ?0}")
    BcProductData findByProductSku(String productName);


    @Query("{supplier : ?0}")
    List<BcProductData> findBySupplier(String supplier);
}