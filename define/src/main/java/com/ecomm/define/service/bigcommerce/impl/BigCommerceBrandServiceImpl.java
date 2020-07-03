package com.ecomm.define.service.bigcommerce.impl;

import com.ecomm.define.domain.bigcommerce.BcBrandData;
import com.ecomm.define.repository.bigcommerce.BigcBrandApiRepository;
import com.ecomm.define.service.bigcommerce.BigCommerceBrandService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BigCommerceBrandServiceImpl implements BigCommerceBrandService {

    @Autowired
    BigcBrandApiRepository repository;

    @Override
    public BcBrandData create(BcBrandData bcCategory) {
        return repository.save(bcCategory);
    }

    @Override
    public BcBrandData findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<BcBrandData> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<BcBrandData> findById(String id) {
        return findById(id);
    }


    @Override
    public List<BcBrandData> findAll() {
        return repository.findAll();
    }

    @Override
    public BcBrandData update(BcBrandData bcCategory) {
        return repository.save(bcCategory);
    }

    @Override
    public void saveAll(List<BcBrandData> bcCategoryList) {
        repository.saveAll(bcCategoryList);
    }


}
