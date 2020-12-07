package com.ecomm.define.suppliers.coreproducts.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.coreproducts.domain.CoreProduct;
import com.ecomm.define.suppliers.coreproducts.domain.CoreProductsStock;
import com.ecomm.define.suppliers.coreproducts.repository.CoreProductsProductRepository;
import com.ecomm.define.suppliers.coreproducts.service.CoreProductService;
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
public class CoreProductServiceImpl implements CoreProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreProductServiceImpl.class);
    private final CoreProductsProductRepository repository;
    private final GenerateBCDataService generateBCDataService;

    private final MongoOperations mongoOperations;


    @Value("${bigcommerce.markharris.profit.percentage.high}")
    private String profitPercentHigh;

    @Value("${bigcommerce.markharris.profit.percentage.low}")
    private String profitPercentLow;

    @Value("${bigcommerce.f2g.vat.percentage}")
    private String vatPercent;

    @Value("${bigcommerce.markharris.profit.limit.high}")
    private int profitLimitHighThreshold;

    @Value("${bigcommerce.markharris.handling.charges.tar1}")
    private int handlignChargesTar1;

    @Value("${bigcommerce.markharris.handling.charges.tar2}")
    private int handlignChargesTar2;

    @Autowired // inject markHarrisData
    public CoreProductServiceImpl(@Lazy @Qualifier("coreProductDataService") GenerateBCDataService generateBCDataService
            , MongoOperations mongoOperations, CoreProductsProductRepository repository) {
        this.repository = repository;
        this.mongoOperations = mongoOperations;
        this.generateBCDataService = generateBCDataService;
    }

    @Override
    public CoreProduct create(CoreProduct coreProduct) {
        return repository.save(coreProduct);
    }

    @Override
    public CoreProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<CoreProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<CoreProduct> findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<CoreProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public CoreProduct update(CoreProduct coreProduct) {
        return repository.save(coreProduct);
    }


    @Override
    public void saveAll(List<CoreProduct> coreProducts) {
        repository.saveAll(coreProducts);
    }


    @Override
    public void uploadProducts(MultipartFile file) {
        LOGGER.info("started uploading CoreProduct from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                // create csv bean reader
                CsvToBean<CoreProduct> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(CoreProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        //.withMappingStrategy(strategy)
                        .build();

                // convert `CsvToBean` object to list of CoreProduct
                List<CoreProduct> coreProducts = csvToBean.parse();
                coreProducts.parallelStream().forEach(this::insertOrUpdate);
            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    private BigDecimal getSalePrice(CoreProduct catalogWithPrice) {
        BigDecimal finalPrice = new BigDecimal(0);
        if (catalogWithPrice != null && catalogWithPrice.getSku() != null && !catalogWithPrice.getSku().isEmpty()) {
            BigDecimal tradePrice = new BigDecimal(catalogWithPrice.getTradePrice());
            BigDecimal delivaryCost = new BigDecimal(catalogWithPrice.getDeliveryCost());
            BigDecimal dhdCost = new BigDecimal(catalogWithPrice.getDhdCost());

            LOGGER.info("SKU --- " + catalogWithPrice.getSku() + " & Price --- " + catalogWithPrice.getPrice());
            finalPrice = finalPrice.add(tradePrice).add(delivaryCost).add(dhdCost)
                    .add(DefineUtils.getVat(finalPrice, new BigDecimal(vatPercent)));
            if (finalPrice.intValue() <= profitLimitHighThreshold) {
                finalPrice = finalPrice.add(DefineUtils.percentage(finalPrice, new BigDecimal(profitPercentHigh))).setScale(0, BigDecimal.ROUND_HALF_UP);
                finalPrice = finalPrice.add(new BigDecimal(handlignChargesTar1));
            } else {
                finalPrice = finalPrice.add(DefineUtils.percentage(finalPrice, new BigDecimal(profitPercentLow))).setScale(0, BigDecimal.ROUND_HALF_UP);
                finalPrice = finalPrice.add(new BigDecimal(handlignChargesTar2));
            }
        }
        return finalPrice;
    }


    private void saveStock(CoreProductsStock stock) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(stock.getSku()));
        int stockQuantity = 0;
        if(!stock.getStockQuantity().contains("0.")) {
            stockQuantity = new BigDecimal(stock.getStockQuantity()).intValue();
        }
        Update update = new Update().inc("matches", 1).set("stockLevel", stockQuantity);
        update.set("isDiscontinued", stock.isDiscontinued()).set("updated", Boolean.TRUE);
        update.set("nextArrival", stock.getArrivalDate());
        CoreProduct andModify = mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(false).upsert(false), CoreProduct.class);
        if(andModify != null) {
            LOGGER.info("Catalog has been updated with stock for sku {}, product name {}", Objects.requireNonNull(andModify).getSku(), andModify.getProductName());
        } else {
            LOGGER.info("Stock not found for the product with sku {}", stock.getSku());

        }
    }

    @Override
    public void uploadProductStockList(MultipartFile file) {
        LOGGER.info("started uploading CoreProduct stock from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean nf
                CsvToBean<CoreProductsStock> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(CoreProductsStock.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of CoreProduct
                List<CoreProductsStock> coreProductsStockList = csvToBean.parse();
                coreProductsStockList.stream().parallel().forEach(this::saveStock);
                LOGGER.info("Successfully Updated stock for all catalog");

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void insertOrUpdate(CoreProduct coreProduct) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(coreProduct.getSku()));
        CoreProduct product = mongoOperations.findOne(query, CoreProduct.class);
        if (product != null) {
            Update update = new Update();
            update.set("discontinued", Boolean.FALSE);
            update.set("updated", Boolean.TRUE);
            update.set("salePrice", getSalePrice(coreProduct));
            UpdateResult updatedProduct = mongoOperations.updateFirst(query, update, CoreProduct.class);
            LOGGER.info("Successfully updated CoreProduct Product SKU {} and ProductName {}", coreProduct.getSku(), coreProduct.getProductName());
        } else {
            coreProduct.setDiscontinued(Boolean.FALSE);
            coreProduct.setUpdated(Boolean.TRUE);
            coreProduct.setImages(coreProduct.getImages());
            coreProduct.setSalePrice(getSalePrice(coreProduct));
            CoreProduct insertedProduct = mongoOperations.insert(coreProduct);
            LOGGER.info("Successfully created CoreProduct Product SKU {} and ProductName {}", insertedProduct.getSku(), insertedProduct.getProductName());
        }
    }

    @Override
    public void uploadCatalogueToBigCommerce() throws Exception {
        Query discontinuedOrModifiedQuery = new Query();
        discontinuedOrModifiedQuery.addCriteria(Criteria.where("updated").is(true));
        List<CoreProduct> coreProducts = mongoOperations.find(discontinuedOrModifiedQuery, CoreProduct.class);
        generateBCDataService.generateBcProductsFromSupplier(coreProducts);

        //Delete the catalog which has been Discontinued from the DB
        Query deleteDiscontinuedCatalogQuery = new Query();
        deleteDiscontinuedCatalogQuery.addCriteria(Criteria.where("isDiscontinued").is(true));
        DeleteResult deleteResult = mongoOperations.remove(deleteDiscontinuedCatalogQuery, CoreProduct.class);
        LOGGER.info("Discontinued Catalog has been deleted from the CoreProduct Table, total records been deleted is {}", deleteResult.getDeletedCount());

    }

}
