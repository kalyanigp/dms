package com.ecomm.define.suppliers.markharris.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.markharris.domain.MarkHarrisPrice;
import com.ecomm.define.suppliers.markharris.domain.MarkHarrisProduct;
import com.ecomm.define.suppliers.markharris.domain.MarkHarrisStock;
import com.ecomm.define.suppliers.markharris.feedgenerator.MarkHarrisFeedMaker;
import com.ecomm.define.suppliers.markharris.repository.MarkHarrisProductRepository;
import com.ecomm.define.suppliers.markharris.service.MarkHarrisService;
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
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class MarkHarrisServiceImpl implements MarkHarrisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarkHarrisServiceImpl.class);
    private final MarkHarrisProductRepository repository;
    private final GenerateBCDataService generateBCDataService;

    private final MongoOperations mongoOperations;


    @Value("${bigcommerce.markharris.profit.percentage.high}")
    private String profitPercentHigh;

    @Autowired // inject markHarrisData
    public MarkHarrisServiceImpl(@Lazy @Qualifier("markHarrisDataService") GenerateBCDataService generateBCDataService
            , MongoOperations mongoOperations, MarkHarrisProductRepository repository) {
        this.repository = repository;
        this.mongoOperations = mongoOperations;
        this.generateBCDataService = generateBCDataService;
    }

    @Override
    public MarkHarrisProduct create(MarkHarrisProduct markHarrisProduct) {
        return repository.save(markHarrisProduct);
    }

    @Override
    public MarkHarrisProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<MarkHarrisProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<MarkHarrisProduct> findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<MarkHarrisProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public MarkHarrisProduct update(MarkHarrisProduct markHarrisProduct) {
        return repository.save(markHarrisProduct);
    }


    @Override
    public void saveAll(List<MarkHarrisProduct> markHarrisProducts) {
        repository.saveAll(markHarrisProducts);
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
                CsvToBean<MarkHarrisProduct> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(MarkHarrisProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MarkHarrisProduct
                List<MarkHarrisProduct> markHarrisProducts = csvToBean.parse();
                markHarrisProducts.parallelStream().forEach(this::insertOrUpdate);
            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    @Override
    public void uploadProductPrice(MultipartFile file) {
        LOGGER.info("started uploading Mark Harris price from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<MarkHarrisPrice> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(MarkHarrisPrice.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Mark Harris
                List<MarkHarrisPrice> markHarrisPrices = csvToBean.parse();
                markHarrisPrices.stream().parallel().forEach(this::savePrice);
                LOGGER.info("Successfully Updated price for all catalog");

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    private void savePrice(MarkHarrisPrice catalogWithPrice) {
        if (catalogWithPrice.getSku() != null && !catalogWithPrice.getSku().isEmpty()) {
            Optional<MarkHarrisProduct> byProductSku = findByProductSku(catalogWithPrice.getSku());
            BigDecimal hdPrice;
            if (byProductSku.isPresent()) {
                MarkHarrisProduct product = byProductSku.get();
                LOGGER.info("SKU --- " + product.getSku() + " & Price --- " + catalogWithPrice.getPrice());

                String priceValue = catalogWithPrice.getPrice().trim();
                if (DefineUtils.isNumeric(priceValue)) {
                    hdPrice = new BigDecimal(priceValue);
                } else {
                    hdPrice = new BigDecimal(priceValue.substring(1));
                }
                if (!product.getPrice().equals(hdPrice)) {
                    product.setUpdated(true);
                    product.setPrice(hdPrice);
                }
                BigDecimal salePrice = hdPrice;

                //add 20% VAT plus 50% Profit
                salePrice = salePrice.add(DefineUtils.getVat(salePrice, new BigDecimal(profitPercentHigh)));
                product.setSalePrice(salePrice);
                product.setHeight(catalogWithPrice.getHeight());
                product.setDepth(catalogWithPrice.getLength());
                product.setWidth(catalogWithPrice.getWidth());
                update(product);
            }
        }
    }

    private void saveStock(MarkHarrisStock stock) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(stock.getSku()));
        Update update = new Update().inc("matches", 1).set("stockLevel", stock.getStockQuantity()).set("stockStatus", stock.getStatus()).set("nextArrival", stock.getArrivalDate());
        if (stock.getStockQuantity() == 0 && stock.getStatus().equals("Discontinued")) {
            update.set("discontinued", Boolean.TRUE).set("isDiscontinued", Boolean.TRUE).set("updated", Boolean.TRUE);
        }
        MarkHarrisProduct andModify = mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(false).upsert(false), MarkHarrisProduct.class);
        LOGGER.info("Catalog has been updated with stock for sku {}, product name {}", Objects.requireNonNull(andModify).getSku(), andModify.getProductName());
    }

    @Override
    public void uploadProductStockList(MultipartFile file) {
        LOGGER.info("started uploading Mark Harris stock from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean nf
                CsvToBean<MarkHarrisStock> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(MarkHarrisStock.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Mark Harris
                List<MarkHarrisStock> markHarrisStockList = csvToBean.parse();
                markHarrisStockList.stream().parallel().forEach(this::saveStock);
                LOGGER.info("Successfully Updated stock for all catalog");

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }


    @Override
    public void insertOrUpdate(MarkHarrisProduct markHarrisProduct) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(markHarrisProduct.getSku()));
        MarkHarrisProduct product = mongoOperations.findOne(query, MarkHarrisProduct.class);
        if (product != null) {
            Update update = new Update();
            update.set("discontinued", Boolean.FALSE);
            update.set("updated", Boolean.TRUE);
            UpdateResult updatedProduct = mongoOperations.updateFirst(query, update, MarkHarrisProduct.class);
            LOGGER.info("Successfully updated MarkHarris Product SKU {} and ProductName {}", markHarrisProduct.getSku(), markHarrisProduct.getProductName());
        } else {
            markHarrisProduct.setDiscontinued(Boolean.FALSE);
            markHarrisProduct.setUpdated(Boolean.TRUE);
            markHarrisProduct.setImages(MarkHarrisFeedMaker.getCatalogImages(markHarrisProduct));
            MarkHarrisProduct insertedProduct = mongoOperations.insert(markHarrisProduct);
            LOGGER.info("Successfully created MarkHarris Product SKU {} and ProductName {}", insertedProduct.getSku(), insertedProduct.getProductName());
        }
    }

    @Override
    public void uploadCatalogueToBigCommerce() throws Exception {
        Query discontinuedOrModifiedQuery = new Query();
        discontinuedOrModifiedQuery.addCriteria(Criteria.where("updated").is(true));
        List<MarkHarrisProduct> markHarrisProducts = mongoOperations.find(discontinuedOrModifiedQuery, MarkHarrisProduct.class);
        generateBCDataService.generateBcProductsFromSupplier(markHarrisProducts);

        //Delete the catalog which has been Discontinued from the DB
        Query deleteDiscontinuedCatalogQuery = new Query();
        deleteDiscontinuedCatalogQuery.addCriteria(Criteria.where("isDiscontinued").is(true));
        DeleteResult deleteResult = mongoOperations.remove(deleteDiscontinuedCatalogQuery, MarkHarrisProduct.class);
        LOGGER.info("Discontinued Catalog has been deleted from the MarkHarrisProduct Table, total records been deleted is {}", deleteResult.getDeletedCount());

    }

}
