package com.ecomm.define.platforms.bigcommerce.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BigcBrandApiRepository extends MongoRepository<BcBrandData, String> {
    BcBrandData findBy_id(ObjectId _id);

    @Override
    Optional<BcBrandData> findById(String s);

    @Query("{id : ?0}")
    Optional<BcBrandData> findById(Integer s);

    @Query("{name : ?0}")
    Optional<BcBrandData> findByName(String s);
    }