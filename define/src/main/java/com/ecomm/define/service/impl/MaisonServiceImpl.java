package com.ecomm.define.service.impl;

import com.ecomm.define.domain.MaisonProducts;
import com.ecomm.define.repository.MaisonProductRepository;
import com.ecomm.define.service.MaisonService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class MaisonServiceImpl implements MaisonService {

    @Autowired
    MaisonProductRepository repository;

    @Override
    public MaisonProducts create(MaisonProducts maisonProduct) {
        return repository.save(maisonProduct);
    }

    @Override
    public MaisonProducts findById(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public MaisonProducts findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<MaisonProducts> findAll() {
        return repository.findAll();
    }

    @Override
    public MaisonProducts update(MaisonProducts maisonProduct) {
        return repository.save(maisonProduct);
    }

    @Override
    public void delete(ObjectId id) {
        repository.delete(findById(id));

    }
}
