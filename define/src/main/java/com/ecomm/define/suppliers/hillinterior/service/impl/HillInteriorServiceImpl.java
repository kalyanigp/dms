package com.ecomm.define.suppliers.hillinterior.service.impl;

import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import com.ecomm.define.suppliers.hillinterior.repository.HillInteriorProductRepository;
import com.ecomm.define.suppliers.hillinterior.service.HillInteriorService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
public class HillInteriorServiceImpl implements HillInteriorService {

    private final HillInteriorProductRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(HillInteriorServiceImpl.class);

    private final BigCommerceApiService bigCommerceApiService;

    private final GenerateBCDataService generateBCDataService;

    private final MongoOperations mongoOperations;

    @Value("${bigcommerce.artisan.profit.percentage.high}")
    private String profitPercentHigh;

    @Autowired // inject hillInteriorDataService
    public HillInteriorServiceImpl(@Lazy @Qualifier("hillInteriorDataService") GenerateBCDataService generateBCDataService
            , MongoOperations mongoOperations, HillInteriorProductRepository repository, BigCommerceApiService bigCommerceApiService) {
        this.repository = repository;
        this.bigCommerceApiService = bigCommerceApiService;
        this.mongoOperations = mongoOperations;
        this.generateBCDataService = generateBCDataService;
    }

    @Override
    public HillInteriorProduct create(HillInteriorProduct hillInteriorProduct) {
        return repository.save(hillInteriorProduct);
    }

    @Override
    public HillInteriorProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<HillInteriorProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<HillInteriorProduct> findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<HillInteriorProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public HillInteriorProduct update(HillInteriorProduct hillInteriorProduct) {
        return repository.save(hillInteriorProduct);
    }

    @Override
    public void saveAll(List<HillInteriorProduct> hillInteriorProductList) {
        repository.saveAll(hillInteriorProductList);
    }


    @Override
    public void uploadProducts(MultipartFile file) {
        LOGGER.info("started uploading MarkHarris from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<HillInteriorProduct> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(HillInteriorProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MarkHarrisProduct
                List<HillInteriorProduct> markHarrisProducts = csvToBean.parse();
                markHarrisProducts.parallelStream().forEach(this::insertOrUpdate);
            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }


    @Override
    public void insertOrUpdate(HillInteriorProduct hillInteriorProduct) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(hillInteriorProduct.getSku()));
        HillInteriorProduct product = mongoOperations.findOne(query, HillInteriorProduct.class);
        if (product != null) {
            if (product.compareTo(hillInteriorProduct) != 0) {
                Update update = new Update();
                update.set("discontinued", Boolean.FALSE);
                update.set("updated", Boolean.TRUE);
                UpdateResult updatedProduct = mongoOperations.updateFirst(query, update, HillInteriorProduct.class);
                LOGGER.info("Successfully updated HillInterior Product SKU {} and ProductName {}", hillInteriorProduct.getSku(), hillInteriorProduct.getProductName());
            }
        } else {
            hillInteriorProduct.setDiscontinued(Boolean.FALSE);
            hillInteriorProduct.setUpdated(Boolean.TRUE);
            HillInteriorProduct insertedProduct = mongoOperations.insert(hillInteriorProduct);
            LOGGER.info("Successfully created HillInterior Product SKU {} and ProductName {}", insertedProduct.getSku(), insertedProduct.getProductName());
        }
    }


    private void processDiscontinuedCatalog(List<HillInteriorProduct> hillInteriorProductList) {
        List<HillInteriorProduct> dbCatalog = findAll();
        List<String> oldCatalog = dbCatalog.stream().map(HillInteriorProduct::getSku).collect(Collectors.toList());
        List<String> newCatalog = hillInteriorProductList.stream().map(HillInteriorProduct::getSku).collect(Collectors.toList());
        List<String> discontinuedList = oldCatalog.stream()
                .filter(e -> !newCatalog.contains(e))
                .collect(Collectors.toList());

        for (String sku : discontinuedList) {
            Optional<HillInteriorProduct> byProductSku = findByProductSku(sku);
            if (byProductSku.isPresent()) {
                HillInteriorProduct hillInteriorProduct = byProductSku.get();
                hillInteriorProduct.setUpdated(Boolean.TRUE);
                hillInteriorProduct.setDiscontinued(Boolean.TRUE);
                repository.save(hillInteriorProduct);
            }
        }

    }

    @Override
    public void uploadCatalogueToBigCommerce() throws Exception {
        Query discontinuedOrModifiedQuery = new Query();
        discontinuedOrModifiedQuery.addCriteria(Criteria.where("updated").is(true));
        List<HillInteriorProduct> hillInteriorProducts = mongoOperations.find(discontinuedOrModifiedQuery, HillInteriorProduct.class);
        generateBCDataService.generateBcProductsFromSupplier(hillInteriorProducts);

        //Delete the catalog which has been Discontinued from the DB
        Query deleteDiscontinuedCatalogQuery = new Query();
        deleteDiscontinuedCatalogQuery.addCriteria(Criteria.where("isDiscontinued").is(true));
        DeleteResult deleteResult = mongoOperations.remove(deleteDiscontinuedCatalogQuery, HillInteriorProduct.class);
        LOGGER.info("Discontinued Catalog has been deleted from the HillInteriorProduct Table, total records been deleted is {}", deleteResult.getDeletedCount());

        //Update modified to false.
        Query updateModifiedCatalogQuery = new Query();
        updateModifiedCatalogQuery.addCriteria(Criteria.where("updated").is(true));
        Update update = new Update();
        update.set("updated", false);
        UpdateResult updateResult = mongoOperations.updateMulti(updateModifiedCatalogQuery, update, HillInteriorProduct.class);
        LOGGER.info("Total number of products modified Updated flag to false is, {}", updateResult.getModifiedCount());
    }
}
