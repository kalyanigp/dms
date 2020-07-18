package com.ecomm.define.suppliers.furniture2go.service.impl;

import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoPrice;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoStock;
import com.ecomm.define.suppliers.furniture2go.repository.Furniture2GoProductRepository;
import com.ecomm.define.suppliers.furniture2go.service.Furniture2GoService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


/**
 * Created by vamshikirangullapelly on 19/04/2020.
 */
@Service
public class Furniture2GoServiceImpl implements Furniture2GoService {

    @Autowired
    Furniture2GoProductRepository repository;

    private final GenerateBCDataService generateBCDataService;

    private static final Logger LOGGER = LoggerFactory.getLogger(Furniture2GoServiceImpl.class);

    private static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";

    @Autowired
    BigCommerceApiService bigCommerceApiService;

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
    public Furniture2GoProduct findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<Furniture2GoProduct> findAll() {
        return repository.findAll();
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
    public Furniture2GoServiceImpl(@Qualifier("furniture2GoDataService") GenerateBCDataService generateBCDataService) {
        this.generateBCDataService = generateBCDataService;
    }

    /*@Override
    public void delete(ObjectId id) {
        repository.delete(findBy_Id(id));

    }*/

    /*@Override
    public List<Furniture2GoProduct> getUpdatedProductList(List<Furniture2GoPrice> newList, List<Furniture2GoPrice> oldList) {
        List<Furniture2GoProduct> furniture2GoList = new ArrayList<>();
        newList.stream().forEach(newProduct -> newProduct.setSku(FURNITURE_2_GO+newProduct.getProductName()));
        for (Furniture2GoProduct furniture2Go : newList) {
            furniture2GoList.addAll(Furniture2GoPredicates.filterProducts(oldList,
                    Furniture2GoPredicates.isPriceQuantityChanged(furniture2Go.getProductName(), furniture2Go.getMspPrice(), furniture2Go.getStockQuantity())));
        }
        return furniture2GoList;
    }*/


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
                        .withType(Furniture2GoProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MaisonProducts
                List<Furniture2GoProduct> furniture2GoProducts = csvToBean.parse();
                repository.saveAll(furniture2GoProducts);

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
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
                furniture2GoPrices.stream().parallel().forEach(price -> savePrice(price));

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    private void savePrice(Furniture2GoPrice price) {
        Furniture2GoProduct byProductSku = repository.findByProductSku(price.getSku());
        if(byProductSku != null) {
            LOGGER.info("Price --- "+price.getPrice());
            byProductSku.setPrice(new BigDecimal(price.getPrice().replace("£","")));
            repository.save(byProductSku);
        }
    }


    private void saveStock(Furniture2GoStock price) {
        Furniture2GoProduct byProductSku = repository.findByProductSku(price.getSku());
        if(byProductSku != null) {
            byProductSku.setStockLevel(price.getStockLevel());
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
                        .withSkipLines(1)
                        .withIgnoreEmptyLine(true)
                        .withType(Furniture2GoStock.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Furniture2GoPrice
                List<Furniture2GoStock> furniture2GoStockList = csvToBean.parse();
                furniture2GoStockList.stream().parallel().forEach(stock -> saveStock(stock));

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
    /*private void deleteDiscontinuedProducts(
            final List<Furniture2GoProduct> newProductList, List<Furniture2GoProduct> oldProductList) throws URISyntaxException {
        List<String> updatedSkus = newProductList.stream().flatMap(p -> Stream.of(p.getProductCode())).collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
        for (Furniture2GoProduct maisonProduct : oldProductList) {
            if (!updatedSkus.contains(maisonProduct.getProductCode())) {
                delete(maisonProduct.get_id());
                BcProductData byProductSku = bigCommerceApiService.findByProductSku(maisonProduct.getProductCode());
                String url = uri + "/" + byProductSku.getId();
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
                LOGGER.info("Successfully Deleted product from Big Commerce due to discontinue, product id {} and product sku {}", byProductSku.getId(), byProductSku.getSku());
                bigCommerceApiService.delete(byProductSku.get_id());
            }
        }
    }*/



}
