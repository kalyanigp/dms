package com.ecomm.define.suppliers.markharris.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;

/**
 * Created by vamshikirangullapelly on 06/07/2020.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarkHarrisPrice {

    @Id
    public ObjectId _id;

    @CsvBindByName(column = "sku")
    private String sku;

    @CsvBindByName(column = "NEW TRADE PRICE EXCL. VAT")
    private String price;

    @CsvBindByName(column = "RANGE")
    private String range;

    @CsvBindByName(column = "LENGTH")
    private BigDecimal length;

    @CsvBindByName(column = "WIDTH")
    private BigDecimal width;

    @CsvBindByName(column = "HEIGHT")
    private BigDecimal height;

}
