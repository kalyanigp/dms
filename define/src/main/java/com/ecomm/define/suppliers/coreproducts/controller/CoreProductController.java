package com.ecomm.define.suppliers.coreproducts.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.coreproducts.domain.CoreProduct;
import com.ecomm.define.suppliers.coreproducts.service.CoreProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/define")
@Api(value = "CoreProductCSVUpload", description = "Uploads Core products and saves in the DB")
public class CoreProductController {

    @Autowired
    private CoreProductService coreProductService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreProductController.class);

    @ApiOperation(value = "Retrieves All CoreProduct Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all CoreProduct products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/coreproduct")
    public List<CoreProduct> getAllProducts() {
        return coreProductService.findAll();
    }

    @ApiOperation(value = "Retrieves CoreProduct Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved CoreProduct product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/coreproduct/{id}")
    public ResponseEntity<CoreProduct> getProductById(@PathVariable("id") ObjectId id) {
        CoreProduct coreProduct = coreProductService.findBy_Id(id);
        if (coreProduct == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(coreProduct);
    }


    @ApiOperation(value = "Uploads Core Product Stock Details from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Core Product Products Stock to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/coreproduct/uploadStock")
    public ResponseEntity<String> uploadCoreProductStockCSVFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Started uploading Stock data from CSV");
        coreProductService.uploadProductStockList(file);
        LOGGER.info("Finished uploading Stock data from CSV");
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }

    @ApiOperation(value = "Uploads Core Products from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Core Product Products Stock to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/coreproduct/uploadProducts")
    public ResponseEntity<String> uploadCoreProductCSVFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Started uploading Master product data from CSV");
        coreProductService.uploadProducts(file);
        LOGGER.info("Finished uploading Master product data from CSV");
        return ResponseEntity.ok().body("Successfully updated Products");
    }

    @ApiOperation(value = "Uploads CoreProduct Catalogue to BigCommerce by reading data from coreProduct table", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded CoreProduct Products to BigCommerce from CoreProduct table"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/coreproduct/bc/products")
    public ResponseEntity<String> uploadCoreProductCataloguetoBigCommerce() throws Exception {
        LOGGER.info("Started uploading CoreProduct products to BigCommerce");
        coreProductService.uploadCatalogueToBigCommerce();
        LOGGER.info("Finished uploading CoreProduct products to BigCommerce");
        return ResponseEntity.ok().body("Successfully CoreProduct Catalogue to BigCommerce");
    }
}