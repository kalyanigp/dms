package com.ecomm.define.service.supplier.maison.impl;

import com.ecomm.define.domain.supplier.maison.MaisonProduct;
import com.ecomm.define.repository.supplier.maison.MaisonProductRepository;
import com.ecomm.define.service.supplier.maison.MaisonService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class MaisonServiceImpl implements MaisonService {

    @Autowired
    MaisonProductRepository repository;

    @Override
    public MaisonProduct create(MaisonProduct maisonProduct) {
        return repository.save(maisonProduct);
    }

    @Override
    public MaisonProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<MaisonProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public MaisonProduct findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<MaisonProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public MaisonProduct update(MaisonProduct maisonProduct) {
        return repository.save(maisonProduct);
    }

    @Override
    public void saveAll(List<MaisonProduct> maisonProductList) {
        repository.saveAll(maisonProductList);
    }


    @Override
    public void delete(ObjectId id) {
        repository.delete(findBy_Id(id));

    }
}
