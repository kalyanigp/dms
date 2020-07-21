package com.ecomm.define.suppliers.furniture2go.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Furniture2GoPrice {

    @CsvBindByName(column = "FTG SKU No.")
    private String sku;
    @CsvBindByName(column = "HD")
    private String price;

}
