package com.ecomm.define.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.domain.MaisonProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MaisonProductRepository extends MongoRepository<MaisonProduct, String> {
    MaisonProduct findBy_id(ObjectId _id);
    @Query("{productCode : ?0}")
    MaisonProduct findByProductSku(String productName);
}