package com.ecomm.define.platforms.bigcommerce.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.platforms.bigcommerce.domain.BcCategoryData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BigcCategoryApiRepository extends MongoRepository<BcCategoryData, String> {
    BcCategoryData findBy_id(ObjectId _id);

    @Override
    Optional<BcCategoryData> findById(String s);

    @Query("{id : ?0}")
    Optional<BcCategoryData> findById(Integer s);

}