package com.ecomm.define.platforms.bigcommerce.service;

import com.ecomm.define.platforms.bigcommerce.domain.BcBrandData;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface BigCommerceBrandService {
    BcBrandData create(final BcBrandData bcCategory);
    BcBrandData findBy_Id(final ObjectId id);
    Optional<BcBrandData> findById(final Integer id);
    Optional<BcBrandData> findById(final String id);
    List<BcBrandData> findAll();
    BcBrandData update(BcBrandData bcCategory);
    void saveAll(List<BcBrandData> bcCategoryList);

}
