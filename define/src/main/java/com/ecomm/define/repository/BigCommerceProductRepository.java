package com.ecomm.define.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.domain.BigCommerceProduct;
import com.ecomm.define.domain.MaisonProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BigCommerceProductRepository extends MongoRepository<BigCommerceProduct, String> {
    BigCommerceProduct findBy_id(ObjectId _id);

    @Override
    Optional<BigCommerceProduct> findById(String s);

    @Query("{productCode : ?0}")
    BigCommerceProduct findByProductSku(String productName);
}