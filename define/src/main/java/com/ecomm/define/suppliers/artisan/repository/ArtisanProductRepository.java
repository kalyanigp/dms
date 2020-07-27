package com.ecomm.define.suppliers.artisan.repository;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ArtisanProductRepository extends MongoRepository<ArtisanProduct, String> {
    ArtisanProduct findBy_id(ObjectId _id);
    @Query("{sku : ?0}")
    Optional<ArtisanProduct> findByProductSku(String productName);
}