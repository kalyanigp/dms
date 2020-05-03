package com.ecomm.define.service.bigcommerce.impl;

import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.repository.bigcommerce.BigcDataApiRepository;
import com.ecomm.define.service.bigcommerce.BigCommerceApiService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BigCommerceApiServiceImpl implements BigCommerceApiService {


    @Autowired
    BigcDataApiRepository repository;

    @Override
    public BcProductData create(BcProductData bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public BcProductData findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<BcProductData> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public BcProductData findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<BcProductData> findAll() {
        return repository.findAll();
    }

    @Override
    public List<BcProductData> findBySupplier(String supplier) {
        return repository.findBySupplier(supplier);
    }

    @Override
    public BcProductData update(BcProductData bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public void saveAll(List<BcProductData> bcProductList) {
        repository.saveAll(bcProductList);
    }


    @Override
    public void delete(ObjectId id) {
        repository.delete(findBy_Id(id));

    }

}
