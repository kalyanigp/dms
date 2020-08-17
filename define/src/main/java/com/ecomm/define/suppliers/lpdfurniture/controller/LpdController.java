package com.ecomm.define.suppliers.lpdfurniture.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.lpdfurniture.domain.LpdProduct;
import com.ecomm.define.suppliers.lpdfurniture.service.LpdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
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
@Api(value = "LpdController", description = "Uploads Lpd products and saves in the DB")
public class LpdController {

    @Autowired
    private LpdService lpdService;

    @ApiOperation(value = "Retrieves All LPD Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all LPD products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/lpd")
    public List<LpdProduct> getAllProducts() {
        return lpdService.findAll();
    }

    @ApiOperation(value = "Retrieves LPD Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved LPD product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/lpd/{id}")
    public ResponseEntity<LpdProduct> getProductById(@PathVariable("id") ObjectId id) {
        LpdProduct lpdProduct = lpdService.findBy_Id(id);
        if (lpdProduct == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(lpdProduct);
    }


    @ApiOperation(value = "Uploads Lpd Products from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded LPD Products Stock to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/lpd/upload-products")
    public ResponseEntity<String> uploadLpdProducts(@RequestParam("file") MultipartFile file) {
        lpdService.uploadProducts(file);
        return ResponseEntity.ok().body("Successfully updated Products");
    }


    @ApiOperation(value = "Uploads CSV File From LPD Price List to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded LPD Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/lpd/products/price")
    public ResponseEntity<String> uploadLpdPriceCSVFile(@RequestParam("file") MultipartFile file) {
        lpdService.uploadProductPrice(file);
        return ResponseEntity.ok().body("Successfully updated Price Feed");
    }

    @ApiOperation(value = "Uploads CSV File From furniture2Go to Product Details DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded furniture2Go Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/lpd/products/stock")
    public ResponseEntity<String> uploadLpdStock(@RequestParam("file") MultipartFile file) {
        lpdService.uploadProductStockList(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }


    @ApiOperation(value = "Uploads lpd Catalogue to BigCommerce by reading data from Lpd table", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Lpd Products to BigCommerce from LpdProduct table"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/lpd/bc/products")
    public ResponseEntity<String> uploadLpdCataloguetoBigCommerce() throws Exception {
        lpdService.uploadCatalogueToBigCommerce();
        return ResponseEntity.ok().body("Successfully LPD Catalogue to BigCommerce");
    }

}