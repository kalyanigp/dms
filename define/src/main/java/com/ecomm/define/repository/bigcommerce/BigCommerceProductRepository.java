package com.ecomm.define.repository.bigcommerce;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.domain.bigcommerce.BigCommerceCsvProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BigCommerceProductRepository extends MongoRepository<BigCommerceCsvProduct, String> {
    BigCommerceCsvProduct findBy_id(ObjectId _id);

    @Override
    Optional<BigCommerceCsvProduct> findById(String s);

    @Query("{productCode : ?0}")
    BigCommerceCsvProduct findByProductSku(String productName);
}