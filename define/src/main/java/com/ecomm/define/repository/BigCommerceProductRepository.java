package com.ecomm.define.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.domain.BigCommerceProducts;
import com.ecomm.define.domain.MaisonProducts;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BigCommerceProductRepository extends MongoRepository<BigCommerceProducts, String> {
    MaisonProducts findBy_id(ObjectId _id);
}