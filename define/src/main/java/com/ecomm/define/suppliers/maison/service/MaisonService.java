package com.ecomm.define.suppliers.maison.service;

import com.ecomm.define.suppliers.maison.domain.MaisonProduct;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

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

    List<MaisonProduct> getUpdatedProductList(List<MaisonProduct> newList, List<MaisonProduct> oldList);

    void uploadProducts(MultipartFile file);
}
