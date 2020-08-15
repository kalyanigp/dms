package com.ecomm.define.platforms.bigcommerce.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceCsvProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BigCommerceProductRepository extends MongoRepository<BigCommerceCsvProduct, String> {
    BigCommerceCsvProduct findBy_id(ObjectId _id);

    @Override
    Optional<BigCommerceCsvProduct> findById(String s);

    @Query("{sku : ?0}")
    BigCommerceCsvProduct findByProductSku(String productName);
}