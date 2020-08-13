package com.ecomm.define.suppliers.maison.service.impl;

import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.maison.domain.MaisonProduct;
import com.ecomm.define.suppliers.maison.repository.MaisonProductRepository;
import com.ecomm.define.suppliers.maison.service.MaisonService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class MaisonServiceImpl implements MaisonService {

    private final MaisonProductRepository repository;

    private final GenerateBCDataService generateBCDataService;

    private final MongoOperations mongoOperations;

    private static final Logger LOGGER = LoggerFactory.getLogger(MaisonServiceImpl.class);


    @Autowired // inject maisonDataService
    public MaisonServiceImpl(@Lazy @Qualifier("maisonDataService") GenerateBCDataService generateBCDataService, BigCommerceApiService bigCommerceApiService, MaisonProductRepository repository, MongoOperations mongoOperations) {
        this.generateBCDataService = generateBCDataService;
        this.bigCommerceApiService = bigCommerceApiService;
        this.repository = repository;
        this.mongoOperations = mongoOperations;
    }

    private final BigCommerceApiService bigCommerceApiService;

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
    public Optional<MaisonProduct> findByProductSku(String sku) {
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
    public void insertOrUpdate(List<MaisonProduct> newList) {
        newList.forEach(catalog -> {
            Query query = new Query();
            query.addCriteria(Criteria.where("productCode").is(catalog.getProductCode()));
            MaisonProduct maisonProduct = mongoOperations.findOne(query, MaisonProduct.class);
            if(maisonProduct != null) {
                if(maisonProduct.compareTo(catalog) != 0) {
                    maisonProduct.setDiscontinued(Boolean.FALSE);
                    mongoOperations.save(maisonProduct);
                    LOGGER.info("Updated Maison Product "+catalog.getProductCode());
                }
            } else {
                catalog.setDiscontinued(Boolean.FALSE);
                catalog.setUpdated(Boolean.TRUE);
                mongoOperations.insert(catalog);
                LOGGER.info("Inserted Maison Product "+catalog.getProductCode());
            }
        });
        LOGGER.info("Successfully updated Maison Products to DB");
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

                // Append MREP to the productCode as to avoid other sellers to trace our product details
                maisonProducts.parallelStream().forEach(catalog ->
                        catalog.setProductCode(catalog.getProductCode()));
                insertOrUpdate(maisonProducts);
                processDiscontinuedCatalog(maisonProducts);

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }



    private void processDiscontinuedCatalog(List<MaisonProduct> maisonProducts) {
        List<MaisonProduct> dbCatalog = findAll();
        List<String> oldCatalog = dbCatalog.stream().map(MaisonProduct::getProductCode).collect(Collectors.toList());
        List<String> newCatalog = maisonProducts.stream().map(MaisonProduct::getProductCode).collect(Collectors.toList());
        List<String> discontinuedList = oldCatalog.stream()
                .filter(e -> !newCatalog.contains(e))
                .collect(Collectors.toList());

        for (String sku : discontinuedList) {
            Optional<MaisonProduct> byProductSku = findByProductSku(sku);
            if (byProductSku.isPresent()) {
                MaisonProduct maisonProduct = byProductSku.get();
                maisonProduct.setUpdated(Boolean.TRUE);
                maisonProduct.setDiscontinued(Boolean.TRUE);
                repository.save(maisonProduct);
            }
        }
    }


    @Override
    public void uploadMaisonCatalogueToBigCommerce() throws Exception {
        Query discontinuedOrModifiedQuery = new Query();
        discontinuedOrModifiedQuery.addCriteria(Criteria.where("isUpdated").is(true));
        List<MaisonProduct> maisonProducts = mongoOperations.find(discontinuedOrModifiedQuery, MaisonProduct.class);
        generateBCDataService.generateBcProductsFromSupplier(maisonProducts);

        //Delete the catalog which has been Discontinued from the DB
        Query deleteDiscontinuedCatalogQuery = new Query();
        deleteDiscontinuedCatalogQuery.addCriteria(Criteria.where("isDiscontinued").is(true));
        DeleteResult deleteResult = mongoOperations.remove(deleteDiscontinuedCatalogQuery, MaisonProduct.class);
        LOGGER.info("Discontinued Catalog has been deleted from the MaisonProduct Table, total records been deleted is {}", deleteResult.getDeletedCount());

        //Update modified to false.
        Query updateModifiedCatalogQuery = new Query();
        updateModifiedCatalogQuery.addCriteria(Criteria.where("isUpdated").is(true));
        Update update = new Update();
        update.set("isUpdated", false);
        UpdateResult updateResult = mongoOperations.updateMulti(updateModifiedCatalogQuery, update, MaisonProduct.class);
        LOGGER.info("Total number of products modified Updated flag to false is, {}", updateResult.getModifiedCount());
    }

}
