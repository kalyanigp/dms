package com.ecomm.define.service.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BcCategoryData;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface BigCommerceCategoryService {
    BcCategoryData create(final BcCategoryData bcCategory);
    BcCategoryData findBy_Id(final ObjectId id);
    Optional<BcCategoryData> findById(final Integer id);
    Optional<BcCategoryData> findById(final String id);
    List<BcCategoryData> findAll();
    BcCategoryData update(BcCategoryData bcCategory);
    void saveAll(List<BcCategoryData> bcCategoryList);

}
