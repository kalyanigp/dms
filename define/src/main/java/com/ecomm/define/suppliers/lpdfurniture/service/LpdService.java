package com.ecomm.define.suppliers.lpdfurniture.service;

import com.ecomm.define.suppliers.lpdfurniture.domain.LpdProduct;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface LpdService {
    LpdProduct create(final LpdProduct lpdProduct);

    LpdProduct findBy_Id(final ObjectId id);

    Optional<LpdProduct> findById(String id);

    Optional<LpdProduct> findByProductSku(final String sku);

    List<LpdProduct> findAll();

    LpdProduct update(LpdProduct hillInteriorProduct);

    void saveAll(List<LpdProduct> hillInteriorProductList);

    //void delete(final ObjectId id);
    //List<Furniture2GoProduct> getUpdatedProductList(List<Furniture2GoProduct> newList, List<Furniture2GoProduct> oldList);
    void uploadProducts(MultipartFile file);

    void uploadProductPrice(MultipartFile file);

    void uploadProductStockList(MultipartFile file);

    void insertOrUpdate(LpdProduct lpdProduct);

    void uploadCatalogueToBigCommerce() throws Exception;
}