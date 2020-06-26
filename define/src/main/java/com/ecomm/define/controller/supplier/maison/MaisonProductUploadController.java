package com.ecomm.define.controller.supplier.maison;

import com.ecomm.define.domain.bigcommerce.BcProductData;
import com.ecomm.define.domain.supplier.maison.MaisonProduct;
import com.ecomm.define.exception.FileNotFoundException;
import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.service.bigcommerce.BigCommerceApiService;
import com.ecomm.define.service.bigcommerce.GenerateBCDataService;
import com.ecomm.define.service.supplier.maison.MaisonService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/define")
@Api(value = "MaisonProductCSVUpload", description = "Uploads Maison products and saves in the DB")
public class MaisonProductUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaisonProductUploadController.class);
    private static final String PRODUCTS_ENDPOINT = "/v3/catalog/products";
    @Autowired
    BigCommerceApiService bigCommerceApiService;
    @Value("${bigcommerce.storehash}")
    private String storeHash;
    @Value("${bigcommerce.access.token}")
    private String accessToken;
    @Value("${bigcommerce.client.id}")
    private String clientId;
    @Value("${bigcommerce.client.baseUrl}")
    private String baseUrl;
    @Autowired
    private MaisonService maisonService;
    @Autowired
    private GenerateBCDataService generateBCDataService;

    @ApiOperation(value = "Retrieves All Maison Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all Maison products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/maison")
    public List<MaisonProduct> getAllProducts() {
        return maisonService.findAll();
    }

    @ApiOperation(value = "Retrieves Maison Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Maison product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/maison/{id}")
    public ResponseEntity<MaisonProduct> getProductById(@PathVariable("id") ObjectId id) {
        MaisonProduct maisonProduct = maisonService.findBy_Id(id);
        if (maisonProduct == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(maisonProduct);
    }


    @ApiOperation(value = "Uploads CSV File From Maison to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Maison Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/maison/upload-csv-file")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {

        LOGGER.info("started uploading from file - {}", file.getOriginalFilename());

        // validate file
        if (file.isEmpty()) {
            throw new FileNotFoundException("Please select a valid CSV file to upload.");
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<MaisonProduct> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(MaisonProduct.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<MaisonProduct> maisonProducts = csvToBean.parse();
                List<MaisonProduct> oldMaisonProducts = maisonService.findAll();
                List<MaisonProduct> updatedProductList = null;
                if (!oldMaisonProducts.isEmpty()) {
                    updatedProductList = maisonService.getUpdatedProductList(maisonProducts, oldMaisonProducts);
                    if (updatedProductList != null) {
                        maisonService.saveAll(updatedProductList);
                        generateBCDataService.generateBcProductsFromMaison(updatedProductList);
                        LOGGER.info("Successfully Updated Stock and Price");
                    }

                    //Check whether any product discontinued and delete them from MaisonProduct table
                    List<MaisonProduct> existingProducts = maisonService.findAll();
                    deleteDiscontinuedProducts(maisonProducts, existingProducts);
                } else {
                    maisonService.saveAll(maisonProducts);
                    generateBCDataService.generateBcProductsFromMaison(maisonProducts);
                    LOGGER.info("Successfully Added New Products from supplier");
                }
            } catch (Exception ex) {
                LOGGER.error("Error while processing CSV File" + ex.getMessage());
            }
        }
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }

    private void deleteDiscontinuedProducts(
            final List<MaisonProduct> newProductList, List<MaisonProduct> oldProductList) throws URISyntaxException {
        List<String> updatedSkus = newProductList.stream().flatMap(p -> Stream.of(p.getProductCode())).collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(baseUrl + storeHash + PRODUCTS_ENDPOINT);
        HttpEntity<BcProductData> request = new HttpEntity<>(null, getHttpHeaders());
        for (MaisonProduct maisonProduct : oldProductList) {
            if (!updatedSkus.contains(maisonProduct.getProductCode())) {
                maisonService.delete(maisonProduct.get_id());
                BcProductData byProductSku = bigCommerceApiService.findByProductSku(maisonProduct.getProductCode());
                String url = uri + "/" + byProductSku.getId();
                restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
                LOGGER.info("Successfully Deleted product from Big Commerce due to discontinue, product id {} and product sku {}", byProductSku.getId(), byProductSku.getSku());
                bigCommerceApiService.delete(byProductSku.get_id());
            }
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", accessToken);
        headers.set("X-Auth-Client", clientId);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }

}