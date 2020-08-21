package com.ecomm.define.platforms.bigcommerce.service.impl;

import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageData;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductImageDataList;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiImage;
import com.ecomm.define.platforms.bigcommerce.domain.BigCommerceApiProduct;
import com.ecomm.define.platforms.bigcommerce.repository.BigcDataApiRepository;
import com.ecomm.define.platforms.bigcommerce.repository.BigcImageDataApiRepository;
import com.ecomm.define.platforms.bigcommerce.service.BigCommerceApiService;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ecomm.define.platforms.bigcommerce.constants.BcConstants.PRODUCTS_ENDPOINT;
import static java.lang.Thread.sleep;

@Service
public class BigCommerceApiServiceImpl implements BigCommerceApiService {

    public static final int BATCH_SIZE = 10;
    private final Logger LOGGER = LoggerFactory.getLogger(BigCommerceApiServiceImpl.class);
    private final RestTemplate restTemplate;
    @Autowired
    BigcDataApiRepository repository;
    @Autowired
    BigcImageDataApiRepository bigcImageDataApiRepository;
    @Autowired
    MongoOperations mongoOperations;
    @Value("${bigcommerce.access.token}")
    private String accessToken;
    @Value("${bigcommerce.client.id}")
    private String clientId;
    @Value("${bigcommerce.storehash}")
    private String storeHash;
    @Value("${bigcommerce.client.baseUrl}")
    private String baseUrl;

    public BigCommerceApiServiceImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public BcProductData create(BcProductData bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public BcProductData findBy_Id(ObjectId id) {
        return repository.findBy_id(id);
    }

    @Override
    public Optional<BcProductData> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public BcProductData findByProductSku(String sku) {
        return repository.findByProductSku(sku);
    }

    @Override
    public List<BcProductData> findAll() {
        return repository.findAll();
    }

    @Override
    public List<BcProductData> findBySupplier(String supplier) {
        return repository.findBySupplier(supplier);
    }

    @Override
    public BcProductData update(BcProductData bcProduct) {
        return repository.save(bcProduct);
    }

    @Override
    public void saveAll(List<BcProductData> bcProductList) {
        repository.saveAll(bcProductList);
    }


    @Override
    public void delete(ObjectId id) {
        repository.delete(findBy_Id(id));

    }

    @Override
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", accessToken);
        headers.set("X-Auth-Client", clientId);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }

    @Override
    public String getStoreHash() {
        return storeHash;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public void populateBigCommerceProduct(List<BcProductData> productDataList, String supplierCode, Object clazz) throws HttpClientErrorException {
        LOGGER.info("Started populating the products to BigCommerce, total number of products to processed is {} for the supplier {}", productDataList.size(), clazz.getClass().getSimpleName());

        productDataList =
                productDataList.stream()
                        .filter(product -> Objects.nonNull(product))
                        .filter(product -> Objects.nonNull(product.getName()))
                        .filter(product -> Objects.nonNull(product.getPrice()))
                        .filter(product -> Objects.nonNull(product.getWeight()))
                        .collect(Collectors.toList());

        int failedProducts = 0;
        int totalProducts = productDataList.size();
        AtomicInteger processedProducts = new AtomicInteger(0);
        try {
            URI uri = new URI(getBaseUrl() + getStoreHash() + PRODUCTS_ENDPOINT);
            List<BcProductData> updateBatchRequest = new ArrayList<>();
            productDataList.stream().forEach(product -> {
                String productSku = product.getSku().replaceAll(supplierCode, "");
                try {
                    HttpEntity<BcProductData> request = new HttpEntity<>(product, getHttpHeaders());
                    if (product.getId() == null) {
                        LOGGER.info("Processing the sku {}, and name {}", product.getSku(), product.getName());
                        BigCommerceApiProduct result = restTemplate.postForObject(uri, request, BigCommerceApiProduct.class);
                        //   sleep(Long.valueOf(30000));

                        BcProductData resultData = Objects.requireNonNull(result).getData();
                        resultData.set_id(product.get_id());
                        resultData.setImageList(product.getImageList());
                        resultData = update(resultData);
                        updateImage(resultData);
                        LOGGER.info("Successfully Created Product in to Big Commerce for the product id {}, sku {} & name {}", resultData.getId(), resultData.getSku(), resultData.getName());

                        //Update modified to false.
                        updateFlag((Class) clazz, productSku);
                        LOGGER.info("Product has been successfully sent to the BigCommerce API ");
                        processedProducts.getAndIncrement();
                    } else {
                        updateBatchRequest.add(product);
                    }


                } catch (HttpClientErrorException httpClientException) {

                    String statusCode = httpClientException.getStatusCode().toString();
                    LOGGER.error("Exception while processing product " + statusCode);
                    Query updateModifiedCatalogQuery = new Query();

                    updateModifiedCatalogQuery.addCriteria(Criteria.where("sku").is(productSku));
                    Update update = new Update();
                    if (statusCode.equals("409 CONFLICT")) {
                        update.set("productName", product.getName() + " " + productSku);
                    }
                    update.set("updated", true);
                    UpdateResult updateResult = mongoOperations.updateFirst(updateModifiedCatalogQuery, update, ((Class) clazz));
                    LOGGER.info("Sku has been updated {} , getMatchedCount {}, getModifiedCount {}", productSku, updateResult.getMatchedCount(), updateResult.getModifiedCount());

                }
            });
            Observable.from(updateBatchRequest).buffer(BATCH_SIZE).forEach((batch) -> processedProducts.addAndGet(processBatchUpdate(batch, uri, supplierCode, (Class) clazz)));
        } catch (Exception exception) {
            exception.printStackTrace();
            LOGGER.error("Exception while uploading to Big Commerce " + exception.toString());
            failedProducts++;
        }
        LOGGER.info("Total products count - {}, Successfully processed count {} & Failed products count  {}", totalProducts, processedProducts, failedProducts);
    }

    private void updateFlag(Class clazz, String productSku) {
        Query updateModifiedCatalogQuery = new Query();
        updateModifiedCatalogQuery.addCriteria(Criteria.where("sku").is(productSku));
        Update update = new Update();
        update.set("updated", false);
        mongoOperations.updateFirst(updateModifiedCatalogQuery, update, clazz);
    }


    private int processBatchUpdate(List<BcProductData> updateBatchRequest, URI uri, String supplierCode, Class clazz) {
        try {
            HttpEntity<List<BcProductData>> batchRequest = new HttpEntity<>(updateBatchRequest, getHttpHeaders());
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, batchRequest, String.class);
            //update the flag to false
            updateBatchRequest.parallelStream().forEach(
                    product -> {
                        String productSku = product.getSku().replaceAll(supplierCode, "");
                        updateFlag((Class) clazz, productSku);
                        LOGGER.info("Successfully updated flag to false for sku {}", productSku);
                    }
            );
            sleep(Long.valueOf(30000));

            LOGGER.info("Successfully Updated batch products in to Big Commerce for the product status code {}", responseEntity.getStatusCode());

        } catch (Exception e) {
            LOGGER.error("Error while processing batch request " + e.getMessage());
            e.printStackTrace();
        }
        return updateBatchRequest.size();
    }

    @Override
    public void processDiscontinuedCatalog(String sku) {
        URI uri;
        try {
            uri = new URI(getBaseUrl() + getStoreHash() + PRODUCTS_ENDPOINT);
            HttpEntity<BcProductData> request = new HttpEntity<>(null, getHttpHeaders());
            BcProductData byProductSku = findByProductSku(sku);
            if (byProductSku != null && byProductSku.getId() != null) {
                String url = uri + "/" + byProductSku.getId();
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
                delete(byProductSku.get_id());
                LOGGER.info("Successfully Deleted product from Big Commerce due to discontinue, product id {} and product sku {}", byProductSku.getId(), byProductSku.getSku());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private void updateImage(BcProductData bcProductData) {
        LOGGER.info("Started uploading images for the product sku {} , name {}", bcProductData.getSku(), bcProductData.getName());
        try {
            URI uri = new URI(getBaseUrl() + getStoreHash() + PRODUCTS_ENDPOINT + "/" + bcProductData.getId() + "/images");
            if (checkIfImagesExists(uri)) {
                BcProductImageData imageData;
                HttpEntity<BcProductImageData> request;
                boolean ifFirstImage = true;
                int sortOrder = 1;
                int imageDesriptionCount = 1;
                BigCommerceApiImage bigCommerceApiImage;
                if (bcProductData.getImageList() != null) {
                    List<String> filteredImagesList = bcProductData.getImageList().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                    for (String image : filteredImagesList) {
                        imageData = new BcProductImageData();
                        imageData.setId(bcProductData.getId());
                        imageData.setSortOrder(sortOrder++);
                        imageData.setIsThumbnail(ifFirstImage);
                        imageData.setDescription("Image_" + imageDesriptionCount++);
                        imageData.setImageUrl(image);
                        request = new HttpEntity<>(imageData, getHttpHeaders());
                        bigCommerceApiImage = restTemplate.postForObject(uri, request, BigCommerceApiImage.class);
                        bigcImageDataApiRepository.save(Objects.requireNonNull(bigCommerceApiImage).getData());
                        ifFirstImage = false;
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Exception occurred while processing images for the product id {}", bcProductData.getId());
            ex.printStackTrace();
        }
        LOGGER.info("Finished uploading images for the product sku {} , name {}", bcProductData.getSku(), bcProductData.getName());
    }

    private boolean checkIfImagesExists(URI uri) {
        ResponseEntity<BcProductImageDataList> responseEntity;
        HttpEntity<BcProductImageDataList> request = new HttpEntity<>(null, getHttpHeaders());
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BcProductImageDataList.class);
        List<BcProductImageData> bcCategoryDataList = Objects.requireNonNull(responseEntity.getBody()).getData();
        return bcCategoryDataList.isEmpty();
    }


}
