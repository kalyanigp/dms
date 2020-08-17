package com.ecomm.define.suppliers.lpdfurniture.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LpdPrice {

    @CsvBindByName(column = "sku")
    private String sku;
    @CsvBindByName(column = "price")
    private BigDecimal price;
    @CsvBindByName(column = "price low")
    private BigDecimal priceLow;



}
