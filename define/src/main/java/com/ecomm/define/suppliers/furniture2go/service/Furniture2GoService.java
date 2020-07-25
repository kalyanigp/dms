package com.ecomm.define.suppliers.furniture2go.service;

import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface Furniture2GoService {
    Furniture2GoProduct create(final Furniture2GoProduct furniture2GoProduct);

    Furniture2GoProduct findBy_Id(final ObjectId id);

    Optional<Furniture2GoProduct> findById(String id);

    Optional<Furniture2GoProduct> findByProductSku(final String sku);

    List<Furniture2GoProduct> findAll();

    List<Furniture2GoProduct> findDiscontinued(final boolean discontinued);

    Furniture2GoProduct update(Furniture2GoProduct furniture2GoProduct);

    void saveAll(List<Furniture2GoProduct> bcProductList);

    void uploadProducts(MultipartFile file);

    void uploadProductPrice(MultipartFile file);

    void uploadProductImages(MultipartFile file);

    void uploadProductStockList(MultipartFile file);

    void insertOrUpdate(Furniture2GoProduct furniture2GoProduct);

    void uploadFurniture2GoCatalogueToBigCommerce() throws Exception;

}
