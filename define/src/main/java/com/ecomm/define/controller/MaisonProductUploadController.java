package com.ecomm.define.controller;

import com.ecomm.define.domain.MaisonProducts;
import com.ecomm.define.service.GenerateBCDataService;
import com.ecomm.define.service.MaisonService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/maison")
public class MaisonProductUploadController {

    @Autowired
    private MaisonService maisonService;

    @Autowired
    private GenerateBCDataService generateBCDataService;

    @GetMapping(value = "/")
    public List<MaisonProducts> getAllProducts() {
        return maisonService.findAll();
    }

    @GetMapping(value = "/{id}")
    public MaisonProducts getProductById(@PathVariable("id") ObjectId id) {
        return maisonService.findById(id);
    }

    @PutMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<MaisonProducts> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(MaisonProducts.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of users
                List<MaisonProducts> maisonProducts = csvToBean.parse();
                for(MaisonProducts product:maisonProducts) {
                    MaisonProducts sku = maisonService.findByProductSku(product.getProductCode());
                    if(sku != null) {
                        maisonService.update(sku);
                    } else {
                        maisonService.create(sku);
                    }
                }

                // save products list on model
                model.addAttribute("products", maisonProducts);
                model.addAttribute("status", true);
                generateBCDataService.generateBcData();

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "file-upload-status";
    }
}