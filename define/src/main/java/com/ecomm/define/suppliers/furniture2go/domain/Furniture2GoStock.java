package com.ecomm.define.suppliers.furniture2go.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Furniture2GoStock {

    @CsvBindByName(column = "ItemNumber")
    private String sku;
    @CsvBindByName(column = "Qty On Hand")
    private int stockLevel;
    @CsvBindByName(column = "Next Delivery Date")
    private String stockArrivalDate;
}
