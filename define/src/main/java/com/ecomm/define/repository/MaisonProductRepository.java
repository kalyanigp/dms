package com.ecomm.define.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.domain.MaisonProducts;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaisonProductRepository extends MongoRepository<MaisonProducts, String> {
    MaisonProducts findBy_id(ObjectId _id);
    @Query("{'productCode : ?0'}")
    MaisonProducts findByProductSku(String productName);
}