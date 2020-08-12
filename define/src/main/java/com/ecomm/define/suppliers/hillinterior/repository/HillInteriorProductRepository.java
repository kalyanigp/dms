package com.ecomm.define.suppliers.hillinterior.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface HillInteriorProductRepository extends MongoRepository<HillInteriorProduct, String> {
    HillInteriorProduct findBy_id(ObjectId _id);

    @Query("{sku : ?0}")
    Optional<HillInteriorProduct> findByProductSku(String productName);
}