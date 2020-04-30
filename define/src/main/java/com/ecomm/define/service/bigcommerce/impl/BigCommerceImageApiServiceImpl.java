package com.ecomm.define.service.bigcommerce.impl;

import com.ecomm.define.domain.bigcommerce.BcProductImageData;
import com.ecomm.define.repository.bigcommerce.BigcImageDataApiRepository;
import com.ecomm.define.service.bigcommerce.BigCommerceImageApiService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BigCommerceImageApiServiceImpl implements BigCommerceImageApiService {


    @Autowired
    BigcImageDataApiRepository repository;


    @Override
    public BcProductImageData create(BcProductImageData bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public BcProductImageData findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<BcProductImageData> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<BcProductImageData> findAll() {
        return repository.findAll();
    }

    @Override
    public BcProductImageData update(BcProductImageData bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public void saveAll(List<BcProductImageData> bcProductList) {

        repository.saveAll(bcProductList);
    }

    @Override
    public void delete(BcProductImageData id) {

        repository.delete(id);
    }
}
