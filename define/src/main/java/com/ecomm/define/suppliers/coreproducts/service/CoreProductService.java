package com.ecomm.define.suppliers.coreproducts.service;

import com.ecomm.define.suppliers.coreproducts.domain.CoreProduct;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface CoreProductService {
    CoreProduct create(final CoreProduct coreProduct);

    CoreProduct findBy_Id(final ObjectId id);

    Optional<CoreProduct> findById(String id);

    Optional<CoreProduct> findByProductSku(final String sku);

    List<CoreProduct> findAll();

    CoreProduct update(CoreProduct coreProduct);

    void saveAll(List<CoreProduct> bcProductList);

    //void delete(final ObjectId id);
    //List<Furniture2GoProduct> getUpdatedProductList(List<Furniture2GoProduct> newList, List<Furniture2GoProduct> oldList);
    void uploadProducts(MultipartFile file);

    //void uploadImages();

    //void uploadImages(MultipartFile file);

    //void uploadProductPrice(MultipartFile file);

    void uploadProductStockList(MultipartFile file);

    void insertOrUpdate(CoreProduct coreProduct);

    void uploadCatalogueToBigCommerce() throws Exception;
}