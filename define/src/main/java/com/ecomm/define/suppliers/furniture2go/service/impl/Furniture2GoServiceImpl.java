package com.ecomm.define.suppliers.furniture2go.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoPrice;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoStock;
import com.ecomm.define.suppliers.furniture2go.feedgenerator.Furniture2GoMasterFeedMaker;
import com.ecomm.define.suppliers.furniture2go.repository.Furniture2GoProductRepository;
import com.ecomm.define.suppliers.furniture2go.service.Furniture2GoService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class Furniture2GoServiceImpl implements Furniture2GoService {

    private final Furniture2GoProductRepository repository;

    private final GenerateBCDataService generateBCDataService;

    private static final Logger LOGGER = LoggerFactory.getLogger(Furniture2GoServiceImpl.class);

    private final MongoOperations mongoOperations;

    @Value("${bigcommerce.f2g.profit.percentage.high}")
    private String profitPercentHigh;

    @Value("${bigcommerce.f2g.profit.percentage.low}")
    private String profitPercentLow;

    @Value("${bigcommerce.f2g.vat.percentage}")
    private String vatPercent;

    @Value("${bigcommerce.f2g.profit.limit.low}")
    private String lowerLimitHDPrice;

    private List<Furniture2GoProduct> newCatalogList = new ArrayList<>();


    @Override
    public Furniture2GoProduct create(Furniture2GoProduct furniture2Go) {
        return repository.save(furniture2Go);
    }

    @Override
    public Furniture2GoProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<Furniture2GoProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Furniture2GoProduct> findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<Furniture2GoProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Furniture2GoProduct> findDiscontinued(boolean discontinued) {
        return repository.findDiscontinued(discontinued);
    }

    @Override
    public Furniture2GoProduct update(Furniture2GoProduct furniture2Go) {
        return repository.save(furniture2Go);
    }

    @Override
    public void saveAll(List<Furniture2GoProduct> furniture2GoList) {
        repository.saveAll(furniture2GoList);
    }


    @Autowired // inject furniture2GoDataService
    public Furniture2GoServiceImpl(@Lazy @Qualifier("furniture2GoDataService") GenerateBCDataService generateBCDataService, Furniture2GoProductRepository repository, BigCommerceApiService bigCommerceApiService, MongoOperations mongoOperations) {
        this.generateBCDataService = generateBCDataService;
        this.repository = repository;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void uploadProducts(MultipartFile file) {
        LOGGER.info("started uploading furniture2Go from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Furniture2GoProduct> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(Furniture2GoProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MaisonProducts
                List<Furniture2GoProduct> furniture2GoProducts = csvToBean.parse();
                furniture2GoProducts.stream().parallel().forEach(this::insertOrUpdate);
                processDiscontinuedCatalog(furniture2GoProducts);
            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    private void processDiscontinuedCatalog(List<Furniture2GoProduct> furniture2GoProducts) {
        List<Furniture2GoProduct> dbCatalog = findAll();
        List<String> oldCatalog = dbCatalog.stream().map(Furniture2GoProduct::getSku).collect(Collectors.toList());
        List<String> newCatalog = furniture2GoProducts.stream().map(Furniture2GoProduct::getSku).collect(Collectors.toList());
        List<String> discontinuedList = oldCatalog.stream()
                .filter(e -> !newCatalog.contains(e))
                .collect(Collectors.toList());

        for (String sku : discontinuedList) {
            Optional<Furniture2GoProduct> byProductSku = findByProductSku(sku);
            if (byProductSku.isPresent()) {
                Furniture2GoProduct furniture2GoProduct = byProductSku.get();
                furniture2GoProduct.setUpdated(Boolean.TRUE);
                furniture2GoProduct.setDiscontinued(Boolean.TRUE);
                repository.save(furniture2GoProduct);
            }
        }

    }

    @Override
    public void uploadProductPrice(MultipartFile file) {
        LOGGER.info("started uploading furniture2Go price from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Furniture2GoPrice> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(Furniture2GoPrice.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Furniture2GoPrice
                List<Furniture2GoPrice> furniture2GoPrices = csvToBean.parse();
                furniture2GoPrices.stream().parallel().forEach(this::savePrice);

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void savePrice(Furniture2GoPrice furniture2GoPrice) {
        if (furniture2GoPrice.getSku() != null && !furniture2GoPrice.getSku().isEmpty()) {
            Optional<Furniture2GoProduct> byProductSku = findByProductSku(furniture2GoPrice.getSku());
            if (byProductSku.isPresent()) {
                Furniture2GoProduct product = byProductSku.get();
                LOGGER.info("SKU --- " + product.getSku() + " & Price --- " + furniture2GoPrice.getPrice());
                BigDecimal price = furniture2GoPrice.getPrice();

                if (product.getPrice() == null) {
                    product.setUpdated(Boolean.TRUE);
                } else if (!product.getPrice().equals(price)) {
                    product.setUpdated(Boolean.TRUE);
                }
                product.setPrice(price);
                BigDecimal salePrice = price;

                salePrice = salePrice.add(DefineUtils.getVat(price, new BigDecimal(vatPercent)));
                if (salePrice.compareTo(new BigDecimal(lowerLimitHDPrice)) < 1) {
                    salePrice = salePrice.add(DefineUtils.percentage(salePrice, new BigDecimal(profitPercentLow))).setScale(0, BigDecimal.ROUND_HALF_UP);
                } else {
                    salePrice = salePrice.add(DefineUtils.percentage(salePrice, new BigDecimal(profitPercentHigh))).setScale(0, BigDecimal.ROUND_HALF_UP);
                }

                product.setSalePrice(salePrice);
                update(product);
            }
        }
    }


    private void saveStock(Furniture2GoStock stock) {
        if (stock.getSku() != null && !stock.getSku().isEmpty()) {
            Optional<Furniture2GoProduct> byProductSku = findByProductSku(stock.getSku());
            if (byProductSku.isPresent()) {
                Furniture2GoProduct product = byProductSku.get();
                if (stock.getStockLevel() != product.getStockLevel()) {
                    LOGGER.info("SKU - " + product.getSku() + " & Stock --- " + stock.getStockLevel());
                    product.setStockLevel(stock.getStockLevel());
                    product.setStockArrivalDate(stock.getStockArrivalDate());
                    product.setUpdated(Boolean.TRUE);
                    update(product);
                }
            }
        }
    }

    @Override
    public void uploadProductStockList(MultipartFile file) {
        LOGGER.info("started uploading furniture2Go stock from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Furniture2GoStock> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(Furniture2GoStock.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Furniture2GoPrice
                List<Furniture2GoStock> furniture2GoStockList = csvToBean.parse();
                furniture2GoStockList.stream().parallel().forEach(this::saveStock);

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void insertOrUpdate(Furniture2GoProduct furniture2GoProduct) {
        Optional<Furniture2GoProduct> byProductSku = findByProductSku(furniture2GoProduct.getSku());
        if (byProductSku.isPresent()) {
            Furniture2GoProduct furnitureTogoProduct = byProductSku.get();
            furnitureTogoProduct.setDiscontinued(Boolean.FALSE);
            update(furnitureTogoProduct);
        } else {
            furniture2GoProduct.setDiscontinued(Boolean.FALSE);
            furniture2GoProduct.setUpdated(Boolean.TRUE);
            furniture2GoProduct.setImages(Furniture2GoMasterFeedMaker.getCatalogImages(furniture2GoProduct));
            Furniture2GoProduct insertedNewCatalog = repository.insert(furniture2GoProduct);
            newCatalogList.add(insertedNewCatalog);
        }
    }

    @Override
    public void uploadFurniture2GoCatalogueToBigCommerce() throws Exception {
        Query discontinuedOrModifiedQuery = new Query();
        discontinuedOrModifiedQuery.addCriteria(Criteria.where("updated").is(true));
        List<Furniture2GoProduct> furniture2GoProducts = mongoOperations.find(discontinuedOrModifiedQuery, Furniture2GoProduct.class);
        generateBCDataService.generateBcProductsFromSupplier(furniture2GoProducts);

        //Delete the catalog which has been Discontinued from the DB
        Query deleteDiscontinuedCatalogQuery = new Query();
        deleteDiscontinuedCatalogQuery.addCriteria(Criteria.where("isDiscontinued").is(true));
        DeleteResult deleteResult = mongoOperations.remove(deleteDiscontinuedCatalogQuery, Furniture2GoProduct.class);
        LOGGER.info("Discontinued Catalog has been deleted from the Furniture2Go Table, total records been deleted is {}", deleteResult.getDeletedCount());

        //Update modified to false.
        Query updateModifiedCatalogQuery = new Query();
        updateModifiedCatalogQuery.addCriteria(Criteria.where("updated").is(true));
        Update update = new Update();
        update.set("updated", false);
        UpdateResult updateResult = mongoOperations.updateMulti(updateModifiedCatalogQuery, update, Furniture2GoProduct.class);
        LOGGER.info("Total number of products modified Updated flag to false is, {}", updateResult.getModifiedCount());
    }

}
