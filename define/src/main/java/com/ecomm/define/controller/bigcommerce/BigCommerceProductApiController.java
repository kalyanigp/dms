package com.ecomm.define.controller.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.domain.bigcommerce.BcProductImageData;
import com.ecomm.define.domain.bigcommerce.BcProductImageDataList;
import com.ecomm.define.domain.bigcommerce.BigCommerceApiProductList;
import com.ecomm.define.service.bigcommerce.BigCommerceApiService;
import com.ecomm.define.service.bigcommerce.BigCommerceImageApiService;
import com.ecomm.define.service.supplier.maison.MaisonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
@RequestMapping("/define/bigcommerce/import")
@Api(value = "BigCommerceCSVGenerator", description = "Operation to generate Big Commerce CSV")
public class BigCommerceProductApiController {
    private final Logger logger = LoggerFactory.getLogger(BigCommerceProductApiController.class);

    public static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";

    @Autowired
    BigCommerceApiService bigCommerceApiService;

    @Autowired
    BigCommerceImageApiService bigCommerceImageApiService;

    @Autowired
    MaisonService maisonService;


    @ApiOperation(value = "Rest call to pull the BigCommerce Products and save them to db, if the product is not present in the db", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products successfully Saved"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 409, message = "Duplicate record found")
    }
    )
    @GetMapping("/all/products")
    public String getAllProducts() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT + "/?limit=300");

        try {
            HttpEntity<BigCommerceApiProductList> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
            ResponseEntity<BigCommerceApiProductList> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BigCommerceApiProductList.class);
            List<BcProductData> bcProductDataList = responseEntity.getBody().getData();
            for (BcProductData bcProductData : bcProductDataList) {
                BcProductData byProductSku = bigCommerceApiService.findByProductSku(bcProductData.getSku());
                if (byProductSku != null) {
                    //TODO Resolve supplier column from BigCommerce Product
                    //byProductSku.setSupplier(Supplier.MAISON.getName());
                    bigCommerceApiService.update(byProductSku);
                } else {
                    //TODO Resolve supplier column from BigCommerce Product
                    //bcProductData.setSupplier(Supplier.MAISON.getName());
                    bigCommerceApiService.create(bcProductData);
                }
            }
            //TODO generate CSV file to take the backup everyday morning after updating all products.
            logger.info("successfully saved the Big Commerce Product Data for Maison");

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
        URI uri = new URI(bigCommerceApiService.getBaseUrl() + bigCommerceApiService.getStoreHash() + PRODUCTS_ENDPOINT);

        try {
            HttpEntity<BcProductImageDataList> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
            List<BcProductData> bigCommerceProducts = bigCommerceApiService.findAll();
            for (BcProductData bcProduct : bigCommerceProducts) {
                ResponseEntity<BcProductImageDataList> responseEntity = restTemplate.exchange(uri + "/" + bcProduct.getId() + "/images", HttpMethod.GET, request, BcProductImageDataList.class);
                List<BcProductImageData> bcCategoryDataList = responseEntity.getBody().getData();
                for (BcProductImageData imageData : bcCategoryDataList) {
                    Optional<BcProductImageData> byId = bigCommerceImageApiService.findById(imageData.getId());
                    if (byId.isPresent()) {
                        bigCommerceImageApiService.update(byId.get());
                    } else {
                        bigCommerceImageApiService.create(imageData);
                    }
                }
            }
            logger.info("successfully saved the Big Commerce Product Data for Maison");
        } catch (Exception ex) {
            logger.error("Exception while saving the categories");
        }
        return "Images successfully Saved";
    }
}