package com.ecomm.define.controller.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BcBrandData;
import com.ecomm.define.domain.bigcommerce.BigCommerceApiBrandList;
import com.ecomm.define.service.bigcommerce.BigCommerceApiService;
import com.ecomm.define.service.bigcommerce.BigCommerceBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;


/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/define/bigcommerce/export")
@Api(value = "BigCommerceCSVGenerator", description = "Operation to generate Big Commerce Brands CSV")
public class BigCommerceBrandController {
    private final Logger logger = LoggerFactory.getLogger(BigCommerceBrandController.class);

    @Value("${bigcommerce.storehash}")
    private String storeHash;

    @Value("${bigcommerce.client.baseUrl}")
    private String baseUrl;
    public static final String BRANDS_ENDPOINT = "/v3/catalog/brands";

    @Autowired
    private BigCommerceBrandService service;

    @Autowired
    private BigCommerceApiService bigCommerceApiService;


    @ApiOperation(value = "Rest call to fetch all brands from BigCommerce and Save in Mongo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Brands successfully fetched"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 409, message = "Duplicate record found")
    }
    )
    @GetMapping("/brands")
    public String getAllBrands() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl + storeHash + BRANDS_ENDPOINT);
            try {
                HttpEntity<BigCommerceApiBrandList> request = new HttpEntity<>(null, bigCommerceApiService.getHttpHeaders());
                ResponseEntity<BigCommerceApiBrandList> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BigCommerceApiBrandList.class);
                List<BcBrandData> bcBrandData = responseEntity.getBody().getData();
                service.saveAll(bcBrandData);
                logger.info("successfully saved all brands");
            } catch (Exception ex) {
                logger.error("Exception while saving the brands");
            }
        return "Successfully Saved Brands";
    }
}