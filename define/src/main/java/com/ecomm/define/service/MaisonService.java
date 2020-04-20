package com.ecomm.define.service;

import com.ecomm.define.domain.MaisonProducts;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface MaisonService {

    MaisonProducts create(final MaisonProducts maisonProduct);
    MaisonProducts findById(final ObjectId id);
    MaisonProducts findByProductSku(final String sku);
    List<MaisonProducts> findAll();
    MaisonProducts update(MaisonProducts maisonProduct);
    void delete(final ObjectId id);

}
