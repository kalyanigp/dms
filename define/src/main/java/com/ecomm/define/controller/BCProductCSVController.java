package com.ecomm.define.controller;

import com.ecomm.define.domain.BigCommerceProducts;
import com.ecomm.define.repository.BigCommerceProductRepository;
import com.ecomm.define.service.ValidateCSVService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/bcproduct")
public class BCProductCSVController {

    @Autowired
    private BigCommerceProductRepository repository;
    @Autowired
    private ValidateCSVService buildCSVService;
    private static final String BIG_COMMERCE_CSV = "big-commerce.csv";


    @GetMapping("/generate-csv-file")
    public String uploadCSVFile( Model model) {

        try (Writer writer = Files.newBufferedWriter(Paths.get(BIG_COMMERCE_CSV))) {
                StatefulBeanToCsv<BigCommerceProducts> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                        .build();

                List<BigCommerceProducts> commerceProductsList = repository.findAll();

            buildCSVService.validate(commerceProductsList);
                beanToCsv.write(commerceProductsList);
                System.out.print("Validated file Exported");

            } catch (Exception ex) {
                model.addAttribute("status", false);
            }
        return "file-7770-status";
    }
}