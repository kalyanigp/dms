package com.ecomm.define.controller;

import com.ecomm.define.domain.BigCommerceProduct;
import com.ecomm.define.service.BigCommerceService;
import com.ecomm.define.service.ValidateCSVService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@RestController
@RequestMapping("/define")
@Api(value = "BigCommerceCSVGenerator", description = "Operation to generate Big Commerce CSV")
public class BigCommerceProductCSVController {

    @Autowired
    private BigCommerceService bcService;
    @Autowired
    private ValidateCSVService validateCSVService;
    private static final String PATH = "./dms/define/";
    private static final String BIG_COMMERCE_CSV = "big-commerce.csv";


    @ApiOperation(value = "Generate CSV File", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully generates csv"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/bcproduct/generate-csv-file")
    public String generateBigCommerceCSVFile() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(PATH + BIG_COMMERCE_CSV), StandardCharsets.UTF_8)) {
            StatefulBeanToCsv<BigCommerceProduct> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                    .withEscapechar(CSVWriter.NO_ESCAPE_CHARACTER)
                    .build();
            List<BigCommerceProduct> commerceProductsList = bcService.findAll();
            commerceProductsList = validateCSVService.validate(commerceProductsList);
            beanToCsv.write(commerceProductsList);
        }
        return "Successfully Generated CSV File";
    }
}