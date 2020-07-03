package com.ecomm.define.controller.bigcommerce;

import com.ecomm.define.domain.bigcommerce.BcCategoryData;
import com.ecomm.define.domain.bigcommerce.BigCommerceApiCategoryList;
import com.ecomm.define.service.bigcommerce.BigCommerceApiService;
import com.ecomm.define.service.bigcommerce.BigCommerceCategoryService;
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
import java.net.URISyntaxException;
import java.util.List;


/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/define/bigcommerce/export")
@Api(value = "BigCommerceCSVGenerator", description = "Operation to generate Big Commerce CSV")
public class BigCommerceCategoryController {
    public static final String CATEGORIES_ENDPOINT = "/v3/catalog/categories/?limit=300";
    private final Logger logger = LoggerFactory.getLogger(BigCommerceCategoryController.class);
    @Autowired
    private BigCommerceCategoryService service;

    @Autowired
    private BigCommerceApiService apiService;


    @ApiOperation(value = "Rest call to fetch the categories from BigCommerce and Save in Mongo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Categories successfully fetched"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 409, message = "Duplicate record found")
    }
    )
    @GetMapping("/categories")
    public String getAllCategories() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BigCommerceApiCategoryList> responseEntity = null;
        try {
            URI uri = new URI(apiService.getBaseUrl() + apiService.getStoreHash() + CATEGORIES_ENDPOINT);
            HttpEntity<BigCommerceApiCategoryList> request = new HttpEntity<>(null, apiService.getHttpHeaders());
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, request, BigCommerceApiCategoryList.class);
            List<BcCategoryData> bcCategoryData = responseEntity.getBody().getData();


            //TODO

            //search a document that doesn't exist Upsert example
            //https://mkyong.com/mongodb/spring-data-mongodb-update-document/
            /*Query query = new Query();
            query.addCriteria(Criteria.where("name").is("appleZ"));

            Update update = new Update();
            update.set("age", 21);

            mongoOperation.upsert(query, update, User.class);

            User userTest5 = mongoOperation.findOne(query, User.class);
            System.out.println("userTest5 - " + userTest5);*/


            bcCategoryData.stream().forEach(categoryData -> service.create(categoryData));
            //service.saveAll(bcCategoryData);
            logger.info("successfully saved the categories");
        } catch (URISyntaxException ex) {
            logger.error("Exception while saving the categories");
        }
        return responseEntity.getStatusCode().getReasonPhrase();
    }
}