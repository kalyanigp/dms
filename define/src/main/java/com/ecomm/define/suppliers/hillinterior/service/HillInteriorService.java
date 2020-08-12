package com.ecomm.define.suppliers.hillinterior.service;

import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface HillInteriorService {
    HillInteriorProduct create(final HillInteriorProduct hillInteriorProduct);

    HillInteriorProduct findBy_Id(final ObjectId id);

    Optional<HillInteriorProduct> findById(String id);

    Optional<HillInteriorProduct> findByProductSku(final String sku);

    List<HillInteriorProduct> findAll();

    HillInteriorProduct update(HillInteriorProduct hillInteriorProduct);

    void saveAll(List<HillInteriorProduct> hillInteriorProductList);

    //void delete(final ObjectId id);
    //List<Furniture2GoProduct> getUpdatedProductList(List<Furniture2GoProduct> newList, List<Furniture2GoProduct> oldList);
    void uploadProducts(MultipartFile file);

    void insertOrUpdate(HillInteriorProduct hillInteriorProduct);

    void uploadCatalogueToBigCommerce() throws Exception;
}