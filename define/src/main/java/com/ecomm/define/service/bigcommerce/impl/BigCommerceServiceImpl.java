package com.ecomm.define.service.bigcommerce.impl;

import com.ecomm.define.domain.BigCommerceProduct;
import com.ecomm.define.repository.BigCommerceProductRepository;
import com.ecomm.define.service.bigcommerce.BigCommerceService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class BigCommerceServiceImpl implements BigCommerceService {

    @Autowired
    BigCommerceProductRepository repository;

    @Override
    public BigCommerceProduct create(BigCommerceProduct bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public BigCommerceProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<BigCommerceProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public BigCommerceProduct findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<BigCommerceProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public BigCommerceProduct update(BigCommerceProduct bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public void saveAll(List<BigCommerceProduct> bcProductList) {
        repository.saveAll(bcProductList);
    }


    @Override
    public void delete(ObjectId id) {
        repository.delete(findBy_Id(id));

    }
}
