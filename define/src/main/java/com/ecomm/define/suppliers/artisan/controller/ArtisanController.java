package com.ecomm.define.suppliers.artisan.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
import com.ecomm.define.suppliers.artisan.service.ArtisanService;
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
@Api(value = "ArtisanCSVUpload", description = "Uploads Artisan products and saves in the DB")
public class ArtisanController {

    @Autowired
    private ArtisanService artisanService;

    @ApiOperation(value = "Retrieves All Furniture2Go Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all Artisan products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/artisan")
    public List<ArtisanProduct> getAllProducts() {
        return artisanService.findAll();
    }

    @ApiOperation(value = "Retrieves artisan Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Artisan product by ID from DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/artisan/{id}")
    public ResponseEntity<ArtisanProduct> getProductById(@PathVariable("id") ObjectId id) {
        ArtisanProduct artisanProduct = artisanService.findBy_Id(id);
        if (artisanProduct == null) {
            throw new RecordNotFoundException("Invalid product id : " + id);
        }
        return ResponseEntity.ok().body(artisanProduct);
    }


    @ApiOperation(value = "Uploads CSV File From artisan Price List to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Artisan Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/artisan/upload-artisan-price")
    public ResponseEntity<String> uploadArtisanPriceCSVFile(@RequestParam("file") MultipartFile file) {
        artisanService.uploadProductPrice(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }



    @ApiOperation(value = "Uploads CSV File From Artisan Stock List to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Artisan Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/artisan/upload-artisan-products")
    public ResponseEntity<String> uploadArtisanCSVFile(@RequestParam("file") MultipartFile file) {
        artisanService.uploadProducts(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }


    @ApiOperation(value = "Uploads CSV File From artisan to Product Details DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Artisan Products to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/artisan/artisan-stocklist")
    public ResponseEntity<String> uploadArtisanProductCSVFile(@RequestParam("file") MultipartFile file) {
        artisanService.uploadProductStockList(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }


}