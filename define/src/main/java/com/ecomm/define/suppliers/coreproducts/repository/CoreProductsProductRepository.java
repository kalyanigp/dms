package com.ecomm.define.suppliers.coreproducts.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.suppliers.coreproducts.domain.CoreProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CoreProductsProductRepository extends MongoRepository<CoreProduct, String> {
    CoreProduct findBy_id(ObjectId _id);
    @Query("{sku : ?0}")
    Optional<CoreProduct> findByProductSku(String productName);
}