package com.ecomm.define.suppliers.markharris.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.markharris.domain.MarkHarrisProduct;
import com.ecomm.define.suppliers.markharris.service.MarkHarrisService;
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
@Api(value = "MarkHarrisCSVUpload", description = "Uploads Mark Harris products and saves in the DB")
public class MarkHarrisController {

    @Autowired
    private MarkHarrisService markHarrisService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkHarrisController.class);

    @ApiOperation(value = "Retrieves All MarkHarris Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all MarkHarris products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/markharris")
    public List<MarkHarrisProduct> getAllProducts() {
        return markHarrisService.findAll();
    }

    @ApiOperation(value = "Retrieves MarkHarris Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved MarkHarris product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/markharris/{id}")
    public ResponseEntity<MarkHarrisProduct> getProductById(@PathVariable("id") ObjectId id) {
        MarkHarrisProduct markHarrisProduct = markHarrisService.findBy_Id(id);
        if (markHarrisProduct == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(markHarrisProduct);
    }


    @ApiOperation(value = "Uploads Mark Harris Price Details from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Mark Harris Products Prices to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/markharris/uploadPrice")
    public ResponseEntity<String> uploadMarkHarrisPriceCSVFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Started uploading Price data from CSV");
        markHarrisService.uploadProductPrice(file);
        LOGGER.info("Finished uploading Price data from CSV");
        return ResponseEntity.ok().body("Successfully updated Price Feed");
    }


    @ApiOperation(value = "Uploads Mark Harris Stock Details from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Mark Harris Products Stock to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/markharris/uploadStock")
    public ResponseEntity<String> uploadMarkHarrisStockCSVFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Started uploading Stock data from CSV");
        markHarrisService.uploadProductStockList(file);
        LOGGER.info("Finished uploading Stock data from CSV");
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }

    @ApiOperation(value = "Uploads Mark Harris Products from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Mark Harris Products Stock to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/markharris/uploadProducts")
    public ResponseEntity<String> uploadMarkHarrisCSVFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Started uploading Master product data from CSV");
        markHarrisService.uploadProducts(file);
        LOGGER.info("Finished uploading Master product data from CSV");
        return ResponseEntity.ok().body("Successfully updated Products");
    }


    @ApiOperation(value = "Uploads Mark Harris Products from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Mark Harris Products Stock to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/markharris/uploadImages")
    public ResponseEntity<String> uploadMarkHarrisImagesCSVFile(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Started uploading Master product Images from CSV");
        markHarrisService.uploadImages(file);
        LOGGER.info("Finished uploading Master product Images from CSV");
        return ResponseEntity.ok().body("Successfully updated Products");
    }


    @ApiOperation(value = "Uploads MarkHarris Catalogue to BigCommerce by reading data from markHarrisProduct table", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded MarkHarris Products to BigCommerce from MarkHarrisProduct table"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/markharris/bc/products")
    public ResponseEntity<String> uploadMarkHarrisCataloguetoBigCommerce() throws Exception {
        LOGGER.info("Started uploading MarkHarris products to BigCommerce");
        markHarrisService.uploadCatalogueToBigCommerce();
        LOGGER.info("Finished uploading MarkHarris products to BigCommerce");
        return ResponseEntity.ok().body("Successfully MarkHarris Catalogue to BigCommerce");
    }
}