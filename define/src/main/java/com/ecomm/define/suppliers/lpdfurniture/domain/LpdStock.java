package com.ecomm.define.suppliers.lpdfurniture.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LpdStock {
    @CsvBindByName(column = "LPD Product Code")
    private String sku;
    @CsvBindByName(column = "Available Qty")
    private int stockLevel;
    @CsvBindByName(column = "Product Description")
    private String productDescription;
}
