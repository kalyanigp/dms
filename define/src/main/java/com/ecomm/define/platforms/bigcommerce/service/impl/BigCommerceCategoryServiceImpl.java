package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.domain.BcCategoryData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcCategoryApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceCategoryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BigCommerceCategoryServiceImpl implements BigCommerceCategoryService {

    @Autowired
    BigcCategoryApiRepository repository;

    @Override
    public BcCategoryData create(BcCategoryData bcCategory) {
        return repository.save(bcCategory);
    }

    @Override
    public BcCategoryData findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<BcCategoryData> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<BcCategoryData> findById(String id) {
        return findById(id);
    }


    @Override
    public List<BcCategoryData> findAll() {
        return repository.findAll();
    }

    @Override
    public BcCategoryData update(BcCategoryData bcCategory) {
        return repository.save(bcCategory);
    }

    @Override
    public void saveAll(List<BcCategoryData> bcCategoryList) {
        repository.saveAll(bcCategoryList);
    }


}
