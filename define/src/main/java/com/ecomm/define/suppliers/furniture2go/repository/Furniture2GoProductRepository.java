package com.ecomm.define.suppliers.furniture2go.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface Furniture2GoProductRepository extends MongoRepository<Furniture2GoProduct, String> {
    Furniture2GoProduct findBy_id(ObjectId _id);
    @Query("{sku : ?0}")
    Furniture2GoProduct findByProductSku(String productName);
}