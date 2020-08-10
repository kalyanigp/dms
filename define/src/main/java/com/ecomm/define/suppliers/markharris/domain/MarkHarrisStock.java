package com.ecomm.define.suppliers.markharris.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarkHarrisStock {
    @CsvBindByName(column = "code")
    private String sku;

    @CsvBindByName(column = "ProductName1")
    private String productName1;

    @CsvBindByName(column = "ProductName2")
    private String productName2;

    @CsvBindByName(column = "Current stock")
    private Integer stockQuantity;

    @CsvBindByName(column = "Status")
    private String status;

    @CsvBindByName(column = "Date 2")
    private String arrivalDate;
}
