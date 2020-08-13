package com.ecomm.define.suppliers.furniture2go.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.furniture2go.domain.Furniture2GoProduct;
import com.ecomm.define.suppliers.furniture2go.service.Furniture2GoService;
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
@Api(value = "Furniture2GoCSVUpload", description = "Uploads Furniture2Go products and saves in the DB")
public class Furniture2GoController {

    @Autowired
    private Furniture2GoService furniture2GoService;

    @ApiOperation(value = "Retrieves All Furniture2Go Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all furniture2Go products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/furniture2Go/products")
    public List<Furniture2GoProduct> getAllProducts() {
        return furniture2GoService.findAll();
    }

    @ApiOperation(value = "Retrieves furniture2Go Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved furniture2Go product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/furniture2Go/products/{id}")
    public ResponseEntity<Furniture2GoProduct> getProductById(@PathVariable("id") ObjectId id) {
        Furniture2GoProduct furniture2Go = furniture2GoService.findBy_Id(id);
        if (furniture2Go == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(furniture2Go);
    }

    @ApiOperation(value = "Uploads CSV File From furniture2Go Stock List to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded furniture2Go Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/furniture2Go/products")
    public ResponseEntity<String> uploadFurniture2GoStockCSVFile(@RequestParam("file") MultipartFile file) {
        furniture2GoService.uploadProducts(file);
        return ResponseEntity.ok().body("Successfully updated Mater Catalog Feed");
    }

    @ApiOperation(value = "Uploads CSV File From furniture2Go Price List to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded furniture2Go Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/furniture2Go/products/price")
    public ResponseEntity<String> uploadFurniture2GoPriceCSVFile(@RequestParam("file") MultipartFile file) {
        furniture2GoService.uploadProductPrice(file);
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
    @PostMapping("/furniture2Go/products/stock")
    public ResponseEntity<String> uploadFurniture2GoProductCSVFile(@RequestParam("file") MultipartFile file) {
        furniture2GoService.uploadProductStockList(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }


    @ApiOperation(value = "Uploads Furniture2Go Catalogue to BigCommerce by reading data from furniture2GoProduct table", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Furniture2Go Products to BigCommerce from furniture2GoProduct table"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/furniture2Go/bc/products")
    public ResponseEntity<String> uploadFurniture2GoCataloguetoBigCommerce() throws Exception {
        furniture2GoService.uploadFurniture2GoCatalogueToBigCommerce();
        return ResponseEntity.ok().body("Successfully pushed Furniture2Go Catalogue to BigCommerce");
    }


}