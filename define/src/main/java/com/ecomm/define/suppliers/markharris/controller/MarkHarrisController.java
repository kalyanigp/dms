package com.ecomm.define.suppliers.markharris.controller;

import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.suppliers.markharris.domain.MarkHarrisProduct;
import com.ecomm.define.suppliers.markharris.service.MarkHarrisService;
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
@Api(value = "MarkHarrisCSVUpload", description = "Uploads Mark Harris products and saves in the DB")
public class MarkHarrisController {

    @Autowired
    private MarkHarrisService markHarrisService;

    @ApiOperation(value = "Retrieves All Artisan Products From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved all Artisan products"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping(value = "/markharris")
    public List<MarkHarrisProduct> getAllProducts() {
        return markHarrisService.findAll();
    }

    @ApiOperation(value = "Retrieves artisan Product by ID From DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Artisan product by ID from DB"),
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


    @ApiOperation(value = "Uploads Mark Harris Price Deatils from CSV File to DB", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Mark Harris Products Images to DB"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/markharris/upload-artisan-price")
    public ResponseEntity<String> uploadArtisanPriceCSVFile(@RequestParam("file") MultipartFile file) {
        markHarrisService.uploadProductPrice(file);
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
    @PostMapping("/markharris/upload-artisan-products")
    public ResponseEntity<String> uploadArtisanCSVFile(@RequestParam("file") MultipartFile file) {
        markHarrisService.uploadProducts(file);
        return ResponseEntity.ok().body("Successfully updated Products");
    }


    @ApiOperation(value = "Uploads Artisan Catalogue to BigCommerce by reading data from ArtisanProduct table", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully uploaded Artisan Products to BigCommerce from ArtisanProduct table"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/markharris/bc/products")
    public ResponseEntity<String> uploadArtisanCataloguetoBigCommerce() throws Exception {
        markHarrisService.uploadCatalogueToBigCommerce();
        return ResponseEntity.ok().body("Successfully Artisan Catalogue to BigCommerce");
    }


}