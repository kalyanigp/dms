package com.ecomm.define.suppliers.artisan.service.impl;

import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.artisan.domain.ArtisanPrice;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.artisan.domain.ArtisanStock;
import com.ecomm.define.suppliers.artisan.feedgenerator.ArtisanMasterFeedMaker;
import com.ecomm.define.suppliers.artisan.repository.ArtisanProductRepository;
import com.ecomm.define.suppliers.artisan.service.ArtisanService;
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
import java.io.IOException;
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
public class ArtisanServiceImpl implements ArtisanService {

    @Autowired
    ArtisanProductRepository repository;

    private final GenerateBCDataService generateBCDataService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtisanServiceImpl.class);

    private static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";

    @Autowired
    BigCommerceApiService bigCommerceApiService;

    @Autowired // inject artisanDataService
    public ArtisanServiceImpl(@Qualifier("artisanDataService") GenerateBCDataService generateBCDataService) {
        this.generateBCDataService = generateBCDataService;
    }

    @Override
    public ArtisanProduct create(ArtisanProduct furniture2Go) {
        return repository.save(furniture2Go);
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
    public ArtisanProduct findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<ArtisanProduct> findAll() {
        return repository.findAll();
    }

    @Override
    public ArtisanProduct update(ArtisanProduct furniture2Go) {
        return repository.save(furniture2Go);
    }

    @Override
    public void saveAll(List<ArtisanProduct> furniture2GoList) {
        repository.saveAll(furniture2GoList);
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
        LOGGER.info("started uploading Artisan Master Data from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file containing EAN Codes  to upload.");
        } else {

            ArtisanMasterFeedMaker feedMaker = new ArtisanMasterFeedMaker();
            List<ArtisanProduct> artsianProductList = null;
            try {
                artsianProductList = feedMaker.processMasterData(file.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            repository.saveAll(artsianProductList);
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
                CsvToBean<ArtisanPrice> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(ArtisanPrice.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Furniture2GoPrice
                List<ArtisanPrice> furniture2GoPrices = csvToBean.parse();
                furniture2GoPrices.stream().parallel().forEach(price -> savePrice(price));

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    private void savePrice(ArtisanPrice price) {
        ArtisanProduct byProductSku = repository.findByProductSku(price.getSku());
        if(byProductSku != null) {
            LOGGER.info("Price --- "+price.getPrice());
            byProductSku.setPrice(new BigDecimal(price.getPrice().replace("£","")));
            repository.save(byProductSku);
        }
    }


    private void saveStock(ArtisanStock price) {
        ArtisanProduct byProductSku = repository.findByProductSku(price.getSku());
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
                CsvToBean<ArtisanStock> csvToBean = new CsvToBeanBuilder(reader)
                        .withSkipLines(1)
                        .withIgnoreEmptyLine(true)
                        .withType(ArtisanStock.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Furniture2GoPrice
                List<ArtisanStock> artisanStockList = csvToBean.parse();
                artisanStockList.stream().parallel().forEach(stock -> saveStock(stock));

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
