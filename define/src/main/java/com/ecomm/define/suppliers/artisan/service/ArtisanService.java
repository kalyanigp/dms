package com.ecomm.define.suppliers.artisan.service;

import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
public interface ArtisanService {
    ArtisanProduct create(final ArtisanProduct artisanProduct);

    ArtisanProduct findBy_Id(final ObjectId id);

    Optional<ArtisanProduct> findById(String id);

    Optional<ArtisanProduct> findByProductSku(final String sku);

    List<ArtisanProduct> findAll();

    ArtisanProduct update(ArtisanProduct artisanProduct);

    void saveAll(List<ArtisanProduct> bcProductList);

    //void delete(final ObjectId id);
    //List<Furniture2GoProduct> getUpdatedProductList(List<Furniture2GoProduct> newList, List<Furniture2GoProduct> oldList);
    void uploadProducts(MultipartFile file);

    void uploadProductPrice(MultipartFile file);

    void uploadProductStockList(MultipartFile file);

    void insertOrUpdate(ArtisanProduct artisanProduct);

    void uploadCatalogueToBigCommerce() throws Exception;
}