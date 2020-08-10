package com.ecomm.define.suppliers.markharris.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.suppliers.markharris.domain.MarkHarrisProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MarkHarrisProductRepository extends MongoRepository<MarkHarrisProduct, String> {
    MarkHarrisProduct findBy_id(ObjectId _id);
    @Query("{sku : ?0}")
    Optional<MarkHarrisProduct> findByProductSku(String productName);
}