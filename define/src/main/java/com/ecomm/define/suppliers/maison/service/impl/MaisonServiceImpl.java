package com.ecomm.define.suppliers.maison.service.impl;

import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.commons.Supplier;
import com.ecomm.define.suppliers.maison.domain.MaisonProduct;
import com.ecomm.define.suppliers.maison.domain.MaisonProductPredicates;
import com.ecomm.define.suppliers.maison.repository.MaisonProductRepository;
import com.ecomm.define.suppliers.maison.service.MaisonService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ecomm.define.platforms.bigcommerce.constants.BcConstants.MAISON_CODE;


/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class MaisonServiceImpl implements MaisonService {

    @Autowired
    MaisonProductRepository repository;

    private final GenerateBCDataService generateBCDataService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MaisonServiceImpl.class);

    private static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";

    @Autowired // inject maisonDataService
    public MaisonServiceImpl(@Lazy @Qualifier("maisonDataService") GenerateBCDataService generateBCDataService) {
        this.generateBCDataService = generateBCDataService;
    }

    @Autowired
    BigCommerceApiService bigCommerceApiService;

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

    @Override
    public List<MaisonProduct> getUpdatedProductList(List<MaisonProduct> newList, List<MaisonProduct> oldList) {
        List<MaisonProduct> priceChangedProducts = new ArrayList<>();
        newList.stream().forEach(newProduct -> newProduct.setProductCode(MAISON_CODE+newProduct.getProductCode()));
        for (MaisonProduct newMaisonProduct : newList) {
            priceChangedProducts.addAll(MaisonProductPredicates.filterProducts(oldList,
                    MaisonProductPredicates.isPriceQuantityChanged(newMaisonProduct.getProductCode(), newMaisonProduct.getMspPrice(), newMaisonProduct.getStockQuantity())));
        }
        return priceChangedProducts;
    }


    @Override
    public void uploadProducts(MultipartFile file) {
        LOGGER.info("started uploading from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<MaisonProduct> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(MaisonProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MaisonProducts
                List<MaisonProduct> maisonProducts = csvToBean.parse();
                List<MaisonProduct> oldMaisonProducts = findAll();
                List<MaisonProduct> updatedProductList;
                if (!oldMaisonProducts.isEmpty()) {
                    updatedProductList = getUpdatedProductList(maisonProducts, oldMaisonProducts);
                    if (updatedProductList != null) {
                        saveAll(updatedProductList);
                        generateBCDataService.generateBcProductsFromSupplier(updatedProductList);
                        LOGGER.info("Successfully Updated Stock and Price");
                    }

                    //Check whether any product discontinued and delete them from MaisonProduct table
                    List<MaisonProduct> existingProducts = findAll();
                    deleteDiscontinuedProducts(maisonProducts, existingProducts);
                } else {
                    maisonProducts.stream().forEach(maisonProd -> maisonProd.setProductCode(MAISON_CODE+maisonProd.getProductCode()));
                    saveAll(maisonProducts);
                    generateBCDataService.generateBcProductsFromSupplier(maisonProducts);
                    LOGGER.info("Successfully Added New Products from supplier"+ Supplier.MAISON.getName());
                }
            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    /**
     * Delete discontinued products from BigCommerce
     * @param newProductList
     * @param oldProductList
     * @throws URISyntaxException
     */
    private void deleteDiscontinuedProducts(
            final List<MaisonProduct> newProductList, List<MaisonProduct> oldProductList) throws URISyntaxException {
        List<String> updatedSkus = newProductList.stream().flatMap(p -> Stream.of(p.getProductCode())).collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
        for (MaisonProduct maisonProduct : oldProductList) {
            if (!updatedSkus.contains(maisonProduct.getProductCode())) {
                delete(maisonProduct.get_id());
                BcProductData byProductSku = bigCommerceApiService.findByProductSku(maisonProduct.getProductCode());
                String url = uri + "/" + byProductSku.getId();
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
                LOGGER.info("Successfully Deleted product from Big Commerce due to discontinue, product id {} and product sku {}", byProductSku.getId(), byProductSku.getSku());
                bigCommerceApiService.delete(byProductSku.get_id());
            }
        }
    }
}
