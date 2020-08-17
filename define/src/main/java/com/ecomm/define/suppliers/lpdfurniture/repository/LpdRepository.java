package com.ecomm.define.suppliers.lpdfurniture.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import com.ecomm.define.suppliers.lpdfurniture.domain.LpdProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface LpdRepository extends MongoRepository<LpdProduct, String> {
    LpdProduct findBy_id(ObjectId _id);

    @Query("{sku : ?0}")
    Optional<LpdProduct> findByProductSku(String productName);
}