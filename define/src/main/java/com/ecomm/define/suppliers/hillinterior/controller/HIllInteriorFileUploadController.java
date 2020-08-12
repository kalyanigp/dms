package com.ecomm.define.suppliers.hillinterior.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.hillinterior.domain.HillInteriorProduct;
import com.ecomm.define.suppliers.hillinterior.service.HillInteriorService;
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
@Api(value = "HillInteriorCSVUpload", description = "Uploads HillInterior products and saves in the DB")
public class HIllInteriorFileUploadController {

    @Autowired
    private HillInteriorService hillInteriorService;

    @ApiOperation(value = "Retrieves All HillInterior Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all HillInterior products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/hillInterior")
    public List<HillInteriorProduct> getAllProducts() {
        return hillInteriorService.findAll();
    }

    @ApiOperation(value = "Retrieves HillInterior Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved HillInterior product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/hillInterior/{id}")
    public ResponseEntity<HillInteriorProduct> getProductById(@PathVariable("id") ObjectId id) {
        HillInteriorProduct hillInteriorProduct = hillInteriorService.findBy_Id(id);
        if (hillInteriorProduct == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(hillInteriorProduct);
    }


    @ApiOperation(value = "Uploads HillInterior Products from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded HillInterior Products Stock to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/hillInterior/upload-hillInterior-products")
    public ResponseEntity<String> uploadHillInteriorCSVFile(@RequestParam("file") MultipartFile file) {
        hillInteriorService.uploadProducts(file);
        return ResponseEntity.ok().body("Successfully updated Products");
    }


    @ApiOperation(value = "Uploads HillInterior Catalogue to BigCommerce by reading data from HillInteriorProduct table", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded HillInterior Products to BigCommerce from HillInteriorProduct table"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/hillInterior/bc/products")
    public ResponseEntity<String> uploadHillInteriorCataloguetoBigCommerce() throws Exception {
        hillInteriorService.uploadCatalogueToBigCommerce();
        return ResponseEntity.ok().body("Successfully HillInterior Catalogue to BigCommerce");
    }


}