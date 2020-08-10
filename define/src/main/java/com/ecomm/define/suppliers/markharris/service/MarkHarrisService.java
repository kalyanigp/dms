package com.ecomm.define.suppliers.markharris.service;

import com.ecomm.define.suppliers.markharris.domain.MarkHarrisProduct;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface MarkHarrisService {
    MarkHarrisProduct create(final MarkHarrisProduct markHarrisProduct);

    MarkHarrisProduct findBy_Id(final ObjectId id);

    Optional<MarkHarrisProduct> findById(String id);

    Optional<MarkHarrisProduct> findByProductSku(final String sku);

    List<MarkHarrisProduct> findAll();

    MarkHarrisProduct update(MarkHarrisProduct markHarrisProduct);

    void saveAll(List<MarkHarrisProduct> bcProductList);

    //void delete(final ObjectId id);
    //List<Furniture2GoProduct> getUpdatedProductList(List<Furniture2GoProduct> newList, List<Furniture2GoProduct> oldList);
    void uploadProducts(MultipartFile file);

    void uploadImages();

    void uploadImages(MultipartFile file);

    void uploadProductPrice(MultipartFile file);

    void uploadProductStockList(MultipartFile file);

    void insertOrUpdate(MarkHarrisProduct markHarrisProduct);

    void uploadCatalogueToBigCommerce() throws Exception;
}