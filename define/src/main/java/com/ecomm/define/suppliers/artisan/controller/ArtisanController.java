package com.ecomm.define.suppliers.artisan.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.artisan.domain.Furniture2GoProduct;
import com.ecomm.define.suppliers.artisan.service.Furniture2GoService;
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
public class ArtisanController {

    @Autowired
    private Furniture2GoService furniture2GoService;

    @ApiOperation(value = "Retrieves All Furniture2Go Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all Maison products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/furniture2Go")
    public List<Furniture2GoProduct> getAllProducts() {
        return furniture2GoService.findAll();
    }

    @ApiOperation(value = "Retrieves furniture2Go Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Maison product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/furniture2Go/{id}")
    public ResponseEntity<Furniture2GoProduct> getProductById(@PathVariable("id") ObjectId id) {
        Furniture2GoProduct furniture2Go = furniture2GoService.findBy_Id(id);
        if (furniture2Go == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(furniture2Go);
    }


    @ApiOperation(value = "Uploads CSV File From furniture2Go Price List to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Maison Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/furniture2Go/upload-furniture2go-price")
    public ResponseEntity<String> uploadFurniture2GoPriceCSVFile(@RequestParam("file") MultipartFile file) {
        furniture2GoService.uploadProductPrice(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }



    @ApiOperation(value = "Uploads CSV File From furniture2Go Stock List to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Maison Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/furniture2Go/upload-furniture2go-products")
    public ResponseEntity<String> uploadFurniture2GoStockCSVFile(@RequestParam("file") MultipartFile file) {
        furniture2GoService.uploadProducts(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }


    @ApiOperation(value = "Uploads CSV File From furniture2Go to Product Details DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Maison Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/furniture2Go/furniture2go-stocklist")
    public ResponseEntity<String> uploadFurniture2GoProductCSVFile(@RequestParam("file") MultipartFile file) {
        furniture2GoService.uploadProductStockList(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }


}