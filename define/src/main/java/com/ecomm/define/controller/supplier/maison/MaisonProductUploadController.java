package com.ecomm.define.controller.supplier.maison;

import com.ecomm.define.domain.supplier.maison.MaisonProduct;
import com.ecomm.define.exception.RecordNotFoundException;
import com.ecomm.define.service.bigcommerce.BigCommerceApiService;
import com.ecomm.define.service.bigcommerce.GenerateBCDataService;
import com.ecomm.define.service.supplier.maison.MaisonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Api(value = "MaisonProductCSVUpload", description = "Uploads Maison products and saves in the DB")
public class MaisonProductUploadController {

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
        maisonService.uploadProducts(file);
        return ResponseEntity.ok().body("Successfully updated Stock Feed");
    }
}