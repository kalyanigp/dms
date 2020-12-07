package com.ecomm.define.suppliers.coreproducts.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoreProductsStock {
    @CsvBindByName(column = "code")
    private String sku;

    @CsvBindByName(column = "Description")
    private String productName;

    @CsvBindByName(column = "Barcode")
    private String ean;

    @CsvBindByName(column = "Free")
    private String stockQuantity;

    @CsvBindByName(column = "Discontinue")
    private String discontinue;

    private boolean isDiscontinued;


    @CsvBindByName(column = "Due Date")
    private String arrivalDate;

    private String status;

    public boolean isDiscontinued() {
        boolean result = false;
        if (getDiscontinue().trim().equalsIgnoreCase("Yes")) {
            result = true;
        }
        return result;
    }
}
