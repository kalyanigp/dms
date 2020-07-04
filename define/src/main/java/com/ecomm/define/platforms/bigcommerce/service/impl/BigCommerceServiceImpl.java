package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceCsvProduct;
import com.ecomm.define.platforms.bigcommerce.repository.BigCommerceProductRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceService;
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
    public BigCommerceCsvProduct create(BigCommerceCsvProduct bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public BigCommerceCsvProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<BigCommerceCsvProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public BigCommerceCsvProduct findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<BigCommerceCsvProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public BigCommerceCsvProduct update(BigCommerceCsvProduct bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public void saveAll(List<BigCommerceCsvProduct> bcProductList) {
        repository.saveAll(bcProductList);
    }


    @Override
    public void delete(ObjectId id) {
        repository.delete(findBy_Id(id));

    }
}
