package com.ecomm.define.suppliers.artisan.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.artisan.domain.ArtisanPrice;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.artisan.domain.ArtisanStock;
import com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanMasterFeedMaker;
import com.ecomm.define.suppliers.artisan.repository.ArtisanProductRepository;
import com.ecomm.define.suppliers.artisan.service.ArtisanService;
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
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class ArtisanServiceImpl implements ArtisanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtisanServiceImpl.class);
    private final ArtisanProductRepository repository;
    private final BigCommerceApiService bigCommerceApiService;

    private final GenerateBCDataService generateBCDataService;

    private final MongoOperations mongoOperations;

    @Value("${bigcommerce.artisan.profit.percentage.high}")
    private String profitPercentHigh;

    @Value("${bigcommerce.vat.percentage}")
    private String vatPercent;

    @Autowired // inject artisanDataService
    public ArtisanServiceImpl(@Qualifier("artisanDataService") GenerateBCDataService generateBCDataService
            , MongoOperations mongoOperations, ArtisanProductRepository repository, BigCommerceApiService bigCommerceApiService) {
        this.repository = repository;
        this.bigCommerceApiService = bigCommerceApiService;
        this.mongoOperations = mongoOperations;
        this.generateBCDataService = generateBCDataService;
    }

    @Override
    public ArtisanProduct create(ArtisanProduct artisanProduct) {
        return repository.save(artisanProduct);
    }

    @Override
    public ArtisanProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<ArtisanProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ArtisanProduct> findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<ArtisanProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public ArtisanProduct update(ArtisanProduct artisanProduct) {
        return repository.save(artisanProduct);
    }

    @Override
    public void saveAll(List<ArtisanProduct> artisanProducts) {
        repository.saveAll(artisanProducts);
    }


    @Override
    public void uploadProducts(MultipartFile file) {
        LOGGER.info("started uploading Artisan Master Data from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file containing EAN Codes  to upload.");
        } else {
            ArtisanMasterFeedMaker feedMaker = new ArtisanMasterFeedMaker();
            List<ArtisanProduct> artsianProductList;
            try {
                artsianProductList = feedMaker.processMasterData(file.getInputStream());
                artsianProductList.stream().parallel().forEach(this::insertOrUpdate);
                processDiscontinuedCatalog(artsianProductList);
            } catch (IOException e) {
                LOGGER.error("Error while processing Artisan Master Catalog {}", e.getCause());
            }
        }
    }

    @Override
    public void uploadProductPrice(MultipartFile file) {
        LOGGER.info("started uploading Artisan price from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<ArtisanPrice> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(ArtisanPrice.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Artisan
                List<ArtisanPrice> artisanPrices = csvToBean.parse();
                artisanPrices.stream().parallel().forEach(this::savePrice);
                LOGGER.info("Successfully Updated price for all catalog");

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void savePrice(ArtisanPrice artisanPriceObj) {
        BigDecimal price = null;
        BigDecimal salePrice;
        if (artisanPriceObj != null && artisanPriceObj.getSku() != null && !artisanPriceObj.getSku().isEmpty()) {
            String priceValue = artisanPriceObj.getPrice().trim();
            if (DefineUtils.isNumeric(priceValue)) {
                price = new BigDecimal(priceValue);
            } else {
                if (priceValue.length()>0) {
                    price = new BigDecimal(priceValue.substring(1));
                }
            }
            Optional<ArtisanProduct> byProductSku = findByProductSku(artisanPriceObj.getSku());
            if (byProductSku.isPresent()) {
                ArtisanProduct artisanProduct = byProductSku.get();
                if (artisanProduct !=null && artisanProduct.getPrice() != null && !artisanProduct.getPrice().equals(price)) {
                    artisanProduct.setUpdated(Boolean.TRUE);
                }
                if (price != null) {
                    artisanProduct.setPrice(price);
                    salePrice = price;
                    salePrice = salePrice.add(DefineUtils.getVat(salePrice, new BigDecimal(vatPercent)));
                    salePrice = salePrice.add(DefineUtils.percentage(salePrice, new BigDecimal(profitPercentHigh))).setScale(0, BigDecimal.ROUND_HALF_UP);
                    artisanProduct.setSalePrice(salePrice);
                }
                update(artisanProduct);
            }

        }
    }

    private void saveStock(ArtisanStock stock) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(stock.getSku()));
        Update update = new Update().inc("matches", 1).set("stockLevel", stock.getStockLevel());
        ArtisanProduct andModify = mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(false).upsert(false), ArtisanProduct.class);
        LOGGER.info("Catalog has been updated with price for sku {}, product name {}", andModify.getSku(), andModify.getProductName());
    }

    @Override
    public void uploadProductStockList(MultipartFile file) {
        LOGGER.info("started uploading Artisan stock from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean nf
                CsvToBean<ArtisanStock> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(ArtisanStock.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Artisan
                List<ArtisanStock> artisanStockList = csvToBean.parse();
                artisanStockList.stream().parallel().forEach(this::saveStock);
                LOGGER.info("Successfully Updated stock for all catalog");

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }


    @Override
    public void insertOrUpdate(ArtisanProduct artisanProduct) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(artisanProduct.getSku()));
        ArtisanProduct product = mongoOperations.findOne(query, ArtisanProduct.class);
        if (product != null) {
            if (product.compareTo(artisanProduct) != 0) {
                Update update = new Update();
                update.set("discontinued", Boolean.FALSE);
                update.set("updated", Boolean.TRUE);
                UpdateResult updatedProduct = mongoOperations.updateFirst(query, update, ArtisanProduct.class);
                LOGGER.info("Successfully updated Artisan Product SKU {} and ProductName {}", artisanProduct.getSku(), artisanProduct.getProductName());
            }
        } else {
            artisanProduct.setDiscontinued(Boolean.FALSE);
            artisanProduct.setUpdated(Boolean.TRUE);
            ArtisanProduct insertedProduct = mongoOperations.insert(artisanProduct);
            LOGGER.info("Successfully created Artisan Product SKU {} and ProductName {}", insertedProduct.getSku(), insertedProduct.getProductName());
        }
    }


    private void processDiscontinuedCatalog(List<ArtisanProduct> artisanProducts) {
        List<ArtisanProduct> dbCatalog = findAll();
        List<String> oldCatalog = dbCatalog.stream().map(ArtisanProduct::getSku).collect(Collectors.toList());
        List<String> newCatalog = artisanProducts.stream().map(ArtisanProduct::getSku).collect(Collectors.toList());
        List<String> discontinuedList = oldCatalog.stream()
                .filter(e -> !newCatalog.contains(e))
                .collect(Collectors.toList());

        for (String sku : discontinuedList) {
            Optional<ArtisanProduct> byProductSku = findByProductSku(sku);
            if (byProductSku.isPresent()) {
                ArtisanProduct artisanProduct = byProductSku.get();
                artisanProduct.setUpdated(Boolean.TRUE);
                artisanProduct.setDiscontinued(Boolean.TRUE);
                repository.save(artisanProduct);
            }
        }

    }

    @Override
    public void uploadCatalogueToBigCommerce() throws Exception {
        Query discontinuedOrModifiedQuery = new Query();
        discontinuedOrModifiedQuery.addCriteria(Criteria.where("updated").is(true));
        List<ArtisanProduct> artisanProducts = mongoOperations.find(discontinuedOrModifiedQuery, ArtisanProduct.class);
        generateBCDataService.generateBcProductsFromSupplier(artisanProducts);

        //Delete the catalog which has been Discontinued from the DB
        Query deleteDiscontinuedCatalogQuery = new Query();
        deleteDiscontinuedCatalogQuery.addCriteria(Criteria.where("isDiscontinued").is(true));
        DeleteResult deleteResult = mongoOperations.remove(deleteDiscontinuedCatalogQuery, ArtisanProduct.class);
        LOGGER.info("Discontinued Catalog has been deleted from the ArtisanProduct Table, total records been deleted is {}", deleteResult.getDeletedCount());

        //Update modified to false.
        Query updateModifiedCatalogQuery = new Query();
        updateModifiedCatalogQuery.addCriteria(Criteria.where("updated").is(true));
        Update update = new Update();
        update.set("updated", false);
        UpdateResult updateResult = mongoOperations.updateMulti(updateModifiedCatalogQuery, update, ArtisanProduct.class);
        LOGGER.info("Total number of products modified Updated flag to false is, {}", updateResult.getModifiedCount());
    }
}
