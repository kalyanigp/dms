package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.repository.BigcImageDataApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceImageApiService;
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

    @Override
    public void insertOrUpdate(BcProductImageData bcProductImageData) {
        Optional<BcProductImageData> imageData = repository.findById(bcProductImageData.getId());
        if(imageData.isPresent()) {
            repository.save(imageData.get());
        } else {
            repository.insert(imageData.get());
        }
    }
}
