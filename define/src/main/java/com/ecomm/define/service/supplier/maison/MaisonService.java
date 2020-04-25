package com.ecomm.define.service.supplier.maison;

import com.ecomm.define.domain.supplier.maison.MaisonProduct;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface MaisonService {
    MaisonProduct create(final MaisonProduct maisonProduct);
    MaisonProduct findBy_Id(final ObjectId id);
    Optional<MaisonProduct> findById(String id);
    MaisonProduct findByProductSku(final String sku);
    List<MaisonProduct> findAll();
    MaisonProduct update(MaisonProduct maisonProduct);
    void saveAll(List<MaisonProduct> bcProductList);
    void delete(final ObjectId id);
}
