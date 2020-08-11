package com.ecomm.define.suppliers.markharris.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.markharris.domain.MarkHarrisImage;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private List<MarkHarrisProduct> newCatalogList = new ArrayList<>();

    @Value("${bigcommerce.artisan.profit.percentage.high}")
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
    public void uploadImages() {
        LOGGER.info("started uploading MarkHarris Images from file ");
        //Process Images
        Map<String, List<String>> catalogImages = MarkHarrisFeedMaker.getCatalogImages(repository.findAll());
        saveImages(catalogImages);
        LOGGER.info("Finished uploading MarkHarris Images from file ");
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
                //Process Images
                //Map<String, List<String>> catalogImages = MarkHarrisFeedMaker.getCatalogImages(repository.findAll());
                //saveImages(catalogImages);

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }


    @Override
    public void uploadImages(MultipartFile file) {
        LOGGER.info("started uploading MarkHarris Imagesvfrom file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<MarkHarrisImage> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(MarkHarrisImage.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MarkHarrisProduct
                List<MarkHarrisImage> markHarrisProducts = csvToBean.parse();
                markHarrisProducts.parallelStream().forEach(image -> {
                    Query query = new Query();
                    query.addCriteria(Criteria.where("sku").is(image.getSku()));
                    MarkHarrisProduct product = mongoOperations.findOne(query, MarkHarrisProduct.class);
                    if (product != null && product.getImages() == null) {
                        List<String> images = new ArrayList<>();
                        images.add(image.getImageURL1());
                        images.add(image.getImageURL2());
                        images.add(image.getImageURL3());
                        images.add(image.getImageURL4());
                        images.add(image.getImageURL5());
                        images.add(image.getImageURL6());
                        images.add(image.getImageURL7());
                        images.add(image.getImageURL8());
                        images.add(image.getImageURL9());
                        images.add(image.getImageURL10());
                        images.add(image.getImageURL11());
                        images.add(image.getImageURL12());
                        images.add(image.getImageURL13());
                        images.add(image.getImageURL14());
                        product.setImages(images);
                        repository.save(product);

                    }
                });


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

                //add 20% VAT plus 30% Profit
                hdPrice = hdPrice.add(DefineUtils.getVat(hdPrice, new BigDecimal(profitPercentHigh)));

                if (!Objects.equals(product.getPrice(), hdPrice)) {
                    product.setUpdated(Boolean.TRUE);
                }
                product.setPrice(hdPrice);
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
        //System.out.println("++++++++++++++++++++++++++++++SKU "+ stock.getSku() + "Status "+ stock.getStatus() + "Stock "+ stock.getStockQuantity());
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
            MarkHarrisProduct insertedProduct = mongoOperations.insert(markHarrisProduct);
            LOGGER.info("Successfully created MarkHarris Product SKU {} and ProductName {}", insertedProduct.getSku(), insertedProduct.getProductName());
        }

        //Process Images
        MarkHarrisProduct markHarrisProd = mongoOperations.findOne(query, MarkHarrisProduct.class);
        if (markHarrisProd.getImages() == null || Objects.requireNonNull(markHarrisProd).getImages().isEmpty()) {
            List<String> catalogImages = MarkHarrisFeedMaker.getCatalogImages(markHarrisProd);
            if (catalogImages != null && catalogImages.isEmpty()) {
                LOGGER.info("Images are null or empty for the sku {} {}", markHarrisProd.getSku(), markHarrisProd.getProductName());
            }
            markHarrisProd.setImages(catalogImages);
            repository.save(markHarrisProd);
        }
    }


    /*private void processDiscontinuedCatalog() {
        List<MarkHarrisProduct> dbCatalog = findAll();
        dbCatalog.parallelStream().forEach(catalog -> {
            if (catalog.getStockLevel() == 0 && catalog.getStockStatus().equals("Discontinued")) {
                catalog.setUpdated(Boolean.TRUE);
                catalog.setDiscontinued(Boolean.TRUE);
                repository.save(catalog);
            }
        });
    }*/

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

        //Update modified to false.
        Query updateModifiedCatalogQuery = new Query();
        updateModifiedCatalogQuery.addCriteria(Criteria.where("updated").is(true));
        Update update = new Update();
        update.set("updated", false);
        UpdateResult updateResult = mongoOperations.updateMulti(updateModifiedCatalogQuery, update, MarkHarrisProduct.class);
        LOGGER.info("Total number of products modified Updated flag to false is, {}", updateResult.getModifiedCount());
    }


    /**
     * Saves Images for the catalog
     *
     * @param images
     */
    private void saveImages(Map<String, List<String>> images) {
        List<MarkHarrisProduct> markHarrisProducts = repository.findAll();
        markHarrisProducts.forEach(product -> {
            if (product.getImages() == null) {
                List<String> imagesList = images.get(product.getSku());
                if (imagesList != null && !imagesList.isEmpty()) {
                    product.setImages(imagesList);
                    product.setUpdated(Boolean.TRUE);
                } else {
                    product.setUpdated(Boolean.TRUE);
                    product.setDiscontinued(Boolean.TRUE);
                }
                repository.save(product);
            }
        });

    }
}
