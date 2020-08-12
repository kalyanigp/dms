package com.ecomm.define.suppliers.hillinterior.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "hillInteriorProduct")
public class HillInteriorProduct {

    @Id
    public ObjectId _id;

    @Indexed(unique = true)
    @CsvBindByName(column = "Code")
    private String sku;

    @CsvBindByName(column = "Colour")
    private String colour;

    @CsvBindByName(column = "Title")
    private String productName;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "Material")
    private String finish;

    @CsvBindByName(column = "BarCode")
    private String ean;


    @CsvBindByName(column = "Width")
    private BigDecimal width;

    @CsvBindByName(column = "Height")
    private BigDecimal height;

    @CsvBindByName(column = "Depth")
    private BigDecimal depth;

    @CsvBindByName(column = "Weight")
    private BigDecimal weight;


    @CsvBindByName(column = "Price")
    private BigDecimal price;


    @CsvBindByName(column = "Available Stock")
    private int stockLevel;

    @CsvBindByName(column = "Stock Expected")
    private String stockExpected;

    @CsvBindByName(column = "Stock Expected On")
    private String stockExpectedOn;

    private List<String> images;

    private boolean updated;

    private boolean isDiscontinued;

    public int compareTo(HillInteriorProduct catalog) {
        int compare = Comparator.comparing(HillInteriorProduct::getSku)
                .thenComparing(HillInteriorProduct::getProductName)
                .thenComparing(HillInteriorProduct::getDescription)
                .thenComparing(HillInteriorProduct::getPrice)
                .thenComparing(HillInteriorProduct::getStockLevel)
                .compare(this, catalog);
        return compare;
    }
}
