package com.ecomm.define.suppliers.furniture2go.service.impl;

import com.ecomm.define.commons.DefineUtils;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.ecomm.define.platforms.bigcommerce.service.GenerateBCDataService;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoImage;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoPrice;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoStock;
import com.ecomm.define.suppliers.furniture2go.repository.Furniture2GoProductRepository;
import com.ecomm.define.suppliers.furniture2go.service.Furniture2GoService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.collections4.map.HashedMap;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @Value("${bigcommerce.f2g.profit.percentage.high}")
    private String profitPercentHigh;

    @Value("${bigcommerce.f2g.profit.percentage.low}")
    private String profitPercentLow;

    @Value("${bigcommerce.f2g.vat.percentage}")
    private String vatPercent;

    @Value("${bigcommerce.f2g.profit.limit.low}")
    private String lowerLimitHDPrice;

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
    public Optional<Furniture2GoProduct> findByProductSku(String sku) {
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
                        .withIgnoreEmptyLine(true)
                        .withType(Furniture2GoProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of MaisonProducts
                List<Furniture2GoProduct> furniture2GoProducts = csvToBean.parse();
                furniture2GoProducts.stream().parallel().forEach(product -> insertOrUpdate(product));
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
        if (price.getSku() != null && !price.getSku().isEmpty()) {
            Optional<Furniture2GoProduct> byProductSku = repository.findByProductSku(price.getSku());
            BigDecimal hdPrice = null;
            BigDecimal hdPriceBeforeVat = null;
            if (byProductSku.isPresent()) {
                Furniture2GoProduct product = byProductSku.get();
                LOGGER.info("SKU - " + product.getSku() + " & Price --- " + price.getPrice());

                String priceValue = price.getPrice().trim();
                if (DefineUtils.isNumeric(priceValue)) {
                    hdPrice = new BigDecimal(priceValue);
                } else {
                    hdPrice = new BigDecimal(priceValue.substring(1));
                }
                hdPriceBeforeVat = hdPrice;
                //add 20% VAT
                hdPrice = hdPrice.add(DefineUtils.getVat(hdPrice, new BigDecimal(vatPercent)));

                //add profit
                //Profit depends on the HD Price, if HDPrice is < 400 then the profit percent is 30 else 40
                if (hdPriceBeforeVat.compareTo(new BigDecimal(lowerLimitHDPrice)) < 1) {
                    hdPrice = hdPrice.add(DefineUtils.percentage(hdPrice, new BigDecimal(profitPercentLow))).setScale(0, BigDecimal.ROUND_HALF_UP);
                } else {
                    hdPrice = hdPrice.add(DefineUtils.percentage(hdPrice, new BigDecimal(profitPercentHigh))).setScale(0, BigDecimal.ROUND_HALF_UP);
                }

                if (product.getPrice() != hdPrice) {
                    product.setUpdated(Boolean.TRUE);
                    product.setPrice(hdPrice);
                    repository.save(product);
                }

                product.setPrice(hdPrice);
                repository.save(product);
            }
        }
    }


    private void saveImage(Map<String, List<String>> images) {
        for (Map.Entry<String, List<String>> entry : images.entrySet()) {
            Optional<Furniture2GoProduct> byProductSku = repository.findByProductSku(entry.getKey());
            if (byProductSku.isPresent()) {
                Furniture2GoProduct furniture2GoProduct = byProductSku.get();
                furniture2GoProduct.setImages(entry.getValue());
                furniture2GoProduct.setUpdated(Boolean.TRUE);
                repository.save(furniture2GoProduct);
            }
        }
    }


    private void saveStock(Furniture2GoStock stock) {
        if (stock.getSku() != null && !stock.getSku().isEmpty()) {
            Optional<Furniture2GoProduct> byProductSku = repository.findByProductSku(stock.getSku());
            if (byProductSku.isPresent()) {
                Furniture2GoProduct product = byProductSku.get();
                if (product != null && stock.getStockLevel() != product.getStockLevel()) {
                    LOGGER.info("SKU - " + product.getSku() + " & Stock --- " + stock.getStockLevel());
                    product.setStockLevel(stock.getStockLevel());
                    product.setUpdated(Boolean.TRUE);
                    repository.save(product);
                }
            }
        }
    }

    @Override
    public void uploadProductImages(MultipartFile file) {
        LOGGER.info("started uploading furniture2Go catalog images from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `Product` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Furniture2GoImage> csvToBean = new CsvToBeanBuilder(reader)
                        .withIgnoreEmptyLine(true)
                        .withType(Furniture2GoImage.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of Furniture2GoPrice
                List<Furniture2GoImage> furniture2GoImages = csvToBean.parse();
                saveImage(populateImagesMap(furniture2GoImages));

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    private Map<String, List<String>> populateImagesMap(List<Furniture2GoImage> furniture2GoImages) {
        Map<String, List<String>> imagesMap = new HashedMap<>();
        for (Furniture2GoImage imageObj : furniture2GoImages) {
            List<String> images = new ArrayList<>();
            images.add(imageObj.getImageURL1());
            images.add(imageObj.getImageURL2());
            images.add(imageObj.getImageURL3());
            images.add(imageObj.getImageURL4());
            images.add(imageObj.getImageURL5());
            images.add(imageObj.getImageURL6());
            images.add(imageObj.getImageURL7());
            images.add(imageObj.getImageURL8());
            images.add(imageObj.getImageURL9());
            images.add(imageObj.getImageURL10());
            images.add(imageObj.getImageURL11());
            images.add(imageObj.getImageURL12());
            images.add(imageObj.getImageURL13());
            images.add(imageObj.getImageURL14());
            imagesMap.put(imageObj.getSku(), images);
        }
        return imagesMap;
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
                furniture2GoStockList.stream().parallel().forEach(stock -> saveStock(stock));

            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
    }

    @Override
    public void insertOrUpdate(Furniture2GoProduct furniture2GoProduct) {
        Optional<Furniture2GoProduct> byProductSku = repository.findByProductSku(furniture2GoProduct.getSku());
        if (byProductSku.isPresent()) {
            repository.save(byProductSku.get());
            LOGGER.info("Duplicate Records Found : " + furniture2GoProduct.getSku());
        } else {
            repository.insert(furniture2GoProduct);
        }
    }

    @Override
    public void uploadFurniture2GoCatalogueToBigCommerce() throws Exception {
        List<Furniture2GoProduct> furniture2GoProducts = repository.findAll();
        generateBCDataService.generateBcProductsFromSupplier(furniture2GoProducts);
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
