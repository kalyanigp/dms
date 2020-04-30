package com.ecomm.define.repository.bigcommerce;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.domain.bigcommerce.BcProductImageData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface BigcImageDataApiRepository extends MongoRepository<BcProductImageData, String> {
    BcProductImageData findBy_id(ObjectId _id);
    @Query("{id : ?0}")
    Optional<BcProductImageData> findById(int id);
}