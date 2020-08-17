package com.ecomm.define.suppliers.lpdfurniture.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.lpdfurniture.domain.LpdPrice;
import com.ecomm.define.suppliers.lpdfurniture.domain.LpdProduct;
import com.ecomm.define.suppliers.lpdfurniture.domain.LpdStock;
import com.ecomm.define.suppliers.lpdfurniture.repository.LpdRepository;
import com.ecomm.define.suppliers.lpdfurniture.service.LpdService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class LpdServiceImpl implements LpdService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LpdServiceImpl.class);
    private final LpdRepository repository;
    private final GenerateBCDataService generateBCDataService;

    private final MongoOperations mongoOperations;

    @Value("${bigcommerce.lpd.profit.percentage.high}")
    private String profitPercentHigh;

    @Value("${bigcommerce.lpd.profit.percentage.low}")
    private String profitPercentLow;

    @Value("${bigcommerce.f2g.vat.percentage}")
    private String vatPercent;

    @Value("${bigcommerce.lpd.profit.limit.low}")
    private String lowerLimitSalePrice;


    @Autowired // inject lpdDataService
    public LpdServiceImpl(@Lazy @Qualifier("lpdDataService") GenerateBCDataService generateBCDataService
            , MongoOperations mongoOperations, LpdRepository repository) {
        this.repository = repository;
        this.mongoOperations = mongoOperations;
        this.generateBCDataService = generateBCDataService;
    }

    @Override
    public LpdProduct create(LpdProduct lpdProduct) {
        return repository.save(lpdProduct);
    }

    @Override
    public LpdProduct findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<LpdProduct> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<LpdProduct> findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<LpdProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public LpdProduct update(LpdProduct lpdProduct) {
        return repository.save(lpdProduct);
    }

    @Override
    public void saveAll(List<LpdProduct> lpdProductList) {
        repository.saveAll(lpdProductList);
    }


    @Override
    public void uploadProducts(MultipartFile file) {
        LOGGER.info("started uploading Lpd Products from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<LpdProduct> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(LpdProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MarkHarrisProduct
                List<LpdProduct> lpdProducts = csvToBean.parse();
                lpdProducts.parallelStream().forEach(this::insertOrUpdate);
                processDiscontinuedCatalog(lpdProducts);

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }


    @Override
    public void uploadProductStockList(MultipartFile file) {
        LOGGER.info("started uploading LPD stock from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<LpdStock> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(LpdStock.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of LpdPrice
                List<LpdStock> lpdStockList = csvToBean.parse();
                lpdStockList.stream().parallel().forEach(this::saveStock);

            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    private void saveStock(LpdStock stock) {
        if (stock.getSku() != null && !stock.getSku().isEmpty()) {
            Optional<LpdProduct> byProductSku = findByProductSku(stock.getSku());
            if (byProductSku.isPresent()) {
                LpdProduct product = byProductSku.get();

                if (product.getStockLevel() == null || stock.getStockLevel() != product.getStockLevel()) {
                    LOGGER.info("SKU - " + product.getSku() + " & Stock --- " + stock.getStockLevel());
                    product.setStockLevel(stock.getStockLevel());
                    product.setUpdated(Boolean.TRUE);
                    update(product);
                }
            }
        }
    }


    private void savePrice(LpdPrice lpdPrice) {
        if (lpdPrice.getSku() != null && !lpdPrice.getSku().isEmpty()) {
            Optional<LpdProduct> byProductSku = findByProductSku(lpdPrice.getSku());
            if (byProductSku.isPresent()) {
                LpdProduct product = byProductSku.get();
                LOGGER.info("SKU --- " + product.getSku() + " & Price --- " + lpdPrice.getPrice());

                BigDecimal price = lpdPrice.getPrice();

                if (product.getPrice() != null && !product.getPrice().equals(price)) {
                    product.setUpdated(Boolean.TRUE);
                }
                product.setPrice(price);
                BigDecimal salePrice = price;

                if (salePrice.compareTo(new BigDecimal(lowerLimitSalePrice)) < 1) {
                    salePrice = salePrice.add(DefineUtils.percentage(salePrice, new BigDecimal(profitPercentLow))).setScale(0, BigDecimal.ROUND_HALF_UP);
                } else {
                    salePrice = salePrice.add(DefineUtils.percentage(salePrice, new BigDecimal(profitPercentHigh))).setScale(0, BigDecimal.ROUND_HALF_UP);
                }
                salePrice = salePrice.add(DefineUtils.getVat(price, new BigDecimal(vatPercent)));

                product.setSalePrice(salePrice);
                update(product);
            }
        }
    }

    @Override
    public void uploadProductPrice(MultipartFile file) {
        LOGGER.info("started uploading LPD price from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<LpdPrice> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(LpdPrice.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of LpdPrice
                List<LpdPrice> lpdPrices = csvToBean.parse();
                lpdPrices = lpdPrices.stream().filter(price -> price != null).collect(Collectors.toList());
                lpdPrices.parallelStream().forEach(this::savePrice);

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File {} ", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void insertOrUpdate(LpdProduct lpdProduct) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(lpdProduct.getSku()));
        LpdProduct product = mongoOperations.findOne(query, LpdProduct.class);
        try {

        if (product != null) {
            if (product.compareTo(lpdProduct) != 0) {
                Update update = new Update();
                update.set("discontinued", Boolean.FALSE);
                update.set("updated", Boolean.TRUE);
                update.set("salePrice", evaluatePrice(product));
                update.set("weight", product.getWeight());
                UpdateResult updatedProduct = mongoOperations.updateFirst(query, update, LpdProduct.class);
                LOGGER.info("Successfully updated Lpd Product SKU {} and ProductName {}", lpdProduct.getSku(), lpdProduct.getProductName());
            }
        } else {
            lpdProduct.setDiscontinued(Boolean.FALSE);
            lpdProduct.setUpdated(Boolean.TRUE);
            lpdProduct.setSalePrice(evaluatePrice(lpdProduct));
            lpdProduct.setWeight(lpdProduct.getWeight());
            LpdProduct insertedProduct = mongoOperations.insert(lpdProduct);
            LOGGER.info("Successfully created Lpd Product SKU {} and ProductName {}", insertedProduct.getSku(), insertedProduct.getProductName());
        }
        } catch(Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while processing product");
        }
    }


    private void processDiscontinuedCatalog(List<LpdProduct> lpdProductList) {
        List<LpdProduct> dbCatalog = findAll();
        List<String> oldCatalog = dbCatalog.stream().map(LpdProduct::getSku).collect(Collectors.toList());
        List<String> newCatalog = lpdProductList.stream().map(LpdProduct::getSku).collect(Collectors.toList());
        List<String> discontinuedList = oldCatalog.stream()
                .filter(e -> !newCatalog.contains(e))
                .collect(Collectors.toList());

        for (String sku : discontinuedList) {
            Optional<LpdProduct> byProductSku = findByProductSku(sku);
            if (byProductSku.isPresent()) {
                LpdProduct lpdProduct = byProductSku.get();
                lpdProduct.setUpdated(Boolean.TRUE);
                lpdProduct.setDiscontinued(Boolean.TRUE);
                repository.save(lpdProduct);
            }
        }

    }

    @Override
    public void uploadCatalogueToBigCommerce() throws Exception {
        Query discontinuedOrModifiedQuery = new Query();
        discontinuedOrModifiedQuery.addCriteria(Criteria.where("updated").is(true));
        List<LpdProduct> lpdProducts = mongoOperations.find(discontinuedOrModifiedQuery, LpdProduct.class);
        generateBCDataService.generateBcProductsFromSupplier(lpdProducts);

        //Delete the catalog which has been Discontinued from the DB
        Query deleteDiscontinuedCatalogQuery = new Query();
        deleteDiscontinuedCatalogQuery.addCriteria(Criteria.where("isDiscontinued").is(true));
        DeleteResult deleteResult = mongoOperations.remove(deleteDiscontinuedCatalogQuery, LpdProduct.class);
        LOGGER.info("Discontinued Catalog has been deleted from the Lpd Table, total records been deleted is {}", deleteResult.getDeletedCount());

    }

    private BigDecimal evaluatePrice(LpdProduct lpdProduct) {
        BigDecimal salePrice = lpdProduct.getPrice();
        if (salePrice != null && salePrice.intValue() > 0) {
            salePrice.add(DefineUtils.getVat(salePrice, new BigDecimal(vatPercent)));
            if (salePrice.intValue() <= 100) {
                salePrice = salePrice.add(DefineUtils.percentage(salePrice, new BigDecimal(profitPercentHigh))).setScale(0, BigDecimal.ROUND_HALF_UP);
            } else {
                salePrice = salePrice.add(DefineUtils.percentage(salePrice, new BigDecimal(profitPercentLow))).setScale(0, BigDecimal.ROUND_HALF_UP);
            }
        }

        return salePrice;
    }
}
