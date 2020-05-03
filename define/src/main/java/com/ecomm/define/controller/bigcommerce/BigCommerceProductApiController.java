package com.ecomm.define.controller.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.domain.bigcommerce.BcProductImageData;
import com.ecomm.define.domain.bigcommerce.BcProductImageDataList;
import com.ecomm.define.domain.bigcommerce.BigCommerceApiProductList;
import com.ecomm.define.service.bigcommerce.BigCommerceApiService;
import com.ecomm.define.service.bigcommerce.BigCommerceImageApiService;
import com.ecomm.define.service.bigcommerce.GenerateBCDataService;
import com.ecomm.define.service.supplier.maison.MaisonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;


/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/define/bigcommerce/export")
@Api(value = "BigCommerceCSVGenerator", description = "Operation to generate Big Commerce CSV")
public class BigCommerceProductApiController {
    private final Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);
    @Value("${bigcommerce.storehash}")
    private String storeHash;
    @Value("${bigcommerce.access.token}")
    private String accessToken;
    @Value("${bigcommerce.client.id}")
    private String clientId;
    @Value("${bigcommerce.client.baseUrl}")
    private String baseUrl;
    public static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";

    @Autowired
    private GenerateBCDataService bcDataService;
    @Autowired
    BigCommerceApiService bigCommerceApiService;

    @Autowired
    BigCommerceImageApiService bigCommerceImageApiService;

    @Autowired
    MaisonService maisonService;



    /*@ApiOperation(value = "Rest call to upload all Maison Products to Big Commerce", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products successfully uploaded to BigCommerce from Maison"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 409, message = "Duplicate record found")
    }
    )
    @PostMapping("/maison/products")
    public String addAllMaisonProducts() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl + storeHash + PRODUCTS_ENDPOINT);
        List<BcProductData> duplicateRecords = new ArrayList<>();
        List<BcProductData> bigCommerceApiProducts = bcDataService.generateBcProductsFromMaison("Maison");
        HttpEntity<BcProductData> request = null;
        BigCommerceApiProduct result = null;
        for (BcProductData product : bigCommerceApiProducts) {
            try {
                request = new HttpEntity<>(product, getHttpHeaders());
                logger.info(request.getBody().getName());
                result = restTemplate.postForObject(uri, request, BigCommerceApiProduct.class);
                BcProductData data = bigCommerceApiService.create(result.getData());
                updateImage(data, restTemplate);
            } catch (Exception duplicateRecordException) {
                logger.error("Duplicate record found with the name {}", request.getBody().getName());
                duplicateRecords.add(product);
                continue;
            }
        }
        if (!duplicateRecords.isEmpty()) {
            logger.info("Found duplicate products while processing maison products. Processing the duplicate products by updating the name attribute");
            processDuplicateRecords(duplicateRecords);
        }
        return "Successfully Generated CSV File";
    }*/


    @ApiOperation(value = "Rest call to pull the BigCommerce Products and save them to db, if the product is not present in the db", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products successfully Saved"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 409, message = "Duplicate record found")
    }
    )
    @GetMapping("/maison/products")
    public String getAllProducts() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl + storeHash + PRODUCTS_ENDPOINT + "/?limit=300");

        try {
            HttpEntity<BigCommerceApiProductList> request = new HttpEntity<>(null, getHttpHeaders());
            ResponseEntity<BigCommerceApiProductList> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BigCommerceApiProductList.class);
            List<BcProductData> bcCategoryDataList = responseEntity.getBody().getData();
            for (BcProductData bcProductData : bcCategoryDataList) {
                BcProductData byProductSku = bigCommerceApiService.findByProductSku(bcProductData.getSku());
                if (byProductSku != null) {
                    byProductSku.setSupplier("Maison");
                    bigCommerceApiService.update(byProductSku);
                } else {
                    bcProductData.setSupplier("Maison");
                    bigCommerceApiService.create(bcProductData);
                }
            }
            logger.info("successfully saved the Big Commerce Product Data for Maison");

            //List<BcCategoryData> categories = result.getBody().getCategories();
        } catch (Exception ex) {
            logger.error("Exception while saving the categories");

        }

        return "Successfully saved the BigCommerce products data.";
    }


    @ApiOperation(value = "Rest call to pull the BigCommerce Product images from BigCommerce and save them in Mongo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Images successfully Saved"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 409, message = "Duplicate record found")
    }
    )
    @GetMapping("/maison/products/images")
    public String getAllProductImages() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl + storeHash + PRODUCTS_ENDPOINT);

        try {
            HttpEntity<BcProductImageDataList> request = new HttpEntity<>(null, getHttpHeaders());
            List<BcProductData> bigCommerceProducts = bigCommerceApiService.findAll();
            for (BcProductData bcProduct : bigCommerceProducts) {
                ResponseEntity<BcProductImageDataList> responseEntity = restTemplate.exchange(uri + "/" + bcProduct.getId() + "/images", HttpMethod.GET, request, BcProductImageDataList.class);
                List<BcProductImageData> bcCategoryDataList = responseEntity.getBody().getData();
                for (BcProductImageData imageData : bcCategoryDataList) {
                    Optional<BcProductImageData> byId = bigCommerceImageApiService.findById(imageData.getId());
                    if (byId.get() == null) {
                        bigCommerceImageApiService.create(imageData);
                    } else {
                        bigCommerceImageApiService.update(byId.get());
                    }
                }
            }
            logger.info("successfully saved the Big Commerce Product Data for Maison");
        } catch (Exception ex) {
            logger.error("Exception while saving the categories");
        }
        return "Images successfully Saved";
    }


    /*private void updateImage(BcProductData data, RestTemplate restTemplate) throws Exception {
        MaisonProduct maisonProduct = maisonService.findByProductSku(data.getSku());
        List<String> images = Arrays.asList(maisonProduct.getImages().split(","));
        URI uri = new URI(baseUrl + storeHash + PRODUCTS_ENDPOINT + "/" + data.getId() + "/images");
        BcProductImageData imageData;
        HttpEntity<BcProductImageData> request = null;
        boolean ifFirstImage = true;
        int sortOrder = 1;
        int imageDesriptionCount = 1;
        BigCommerceApiImage bigCommerceApiImage;
        List<String> filteredImagesList = images.stream().filter(image -> StringUtils.isNotEmpty(image)).collect(Collectors.toList());
        for (String image : filteredImagesList) {
            try {
                imageData = new BcProductImageData();
                imageData.setId(data.getId());
                imageData.setSortOrder(sortOrder++);
                imageData.setIsThumbnail(ifFirstImage);
                imageData.setDescription("Image_" + imageDesriptionCount++);
                imageData.setImageUrl(image);
                request = new HttpEntity<>(imageData, getHttpHeaders());
                bigCommerceApiImage = restTemplate.postForObject(uri, request, BigCommerceApiImage.class);
                bigCommerceImageApiService.create(bigCommerceApiImage.getData());
                ifFirstImage = false;
            } catch (Exception ex) {
                logger.error("Exception occurred while processing images for the product id {}", data.getId());
                continue;
            }
        }
    }*/


   /* private void processDuplicateRecords(List<BcProductData> duplicateRecords) throws Exception {
        logger.info("Started processing duplicate records for Maison");
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl + storeHash + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = null;
        List<BcProductData> duplicateRecords1 = new ArrayList<>();
        BigCommerceApiProduct result = null;
        for (BcProductData data : duplicateRecords) {
            logger.info("Started processing duplicate record for product sku {} and product name {}", data.getSku(), data.getName());
            MaisonProduct byProductSku = maisonService.findByProductSku(data.getSku());
            byProductSku.setTitle(byProductSku.getTitle() + " " + byProductSku.getProductCode());
            MaisonProduct updatedMaisonProduct = maisonService.update(byProductSku);
            data.setName(updatedMaisonProduct.getTitle());
            try {
                request = new HttpEntity<>(data, getHttpHeaders());
                logger.info(request.getBody().getName());
                result = restTemplate.postForObject(uri, request, BigCommerceApiProduct.class);
                BcProductData bcProductData = bigCommerceApiService.create(result.getData());
                updateImage(bcProductData, restTemplate);
            } catch (Exception exception) {
                logger.error("Duplicate record found with the name {}", request.getBody().getName());
                duplicateRecords1.add(result.getData());
                continue;
            }

        }
        if (!duplicateRecords1.isEmpty()) {
            processDuplicateRecords(duplicateRecords1);
        }
        logger.info("Successfully finished processing the duplicate records for Maison");
    }*/


    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", accessToken);
        headers.set("X-Auth-Client", clientId);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }
}