package com.ecomm.define.suppliers.artisan.domain;

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
@Document(collection = "artisanProduct")
public class ArtisanProduct {

    @Id
    public ObjectId _id;

    @Indexed(unique = true)
    @CsvBindByName(column = "SKU")
    private String sku;

    @CsvBindByName(column = "Range")
    private String range;


    @CsvBindByName(column = "ProductName")
    private String productName;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "Finish")
    private String finish;

    @CsvBindByName(column = "EAN")
    private String ean;


    @CsvBindByName(column = "Width")
    private BigDecimal width;

    @CsvBindByName(column = "Height")
    private BigDecimal height;

    @CsvBindByName(column = "Depth")
    private BigDecimal depth;

    @CsvBindByName(column = "Weight")
    private BigDecimal weight;

    @CsvBindByName(column = "NoOfBoxes")
    private int noOfBoxes;

    @CsvBindByName(column = "BP1")
    private String bp1;

    @CsvBindByName(column = "BP2")
    private String bp2;

    @CsvBindByName(column = "BP3")
    private String bp3;

    @CsvBindByName(column = "BP4")
    private String bp4;

    @CsvBindByName(column = "BP5")
    private String bp5;

    @CsvBindByName(column = "BP6")
    private String bp6;

    @CsvBindByName(column = "AssemblyInstructions")
    private String assemblyInstructions;

    private BigDecimal price;

    private BigDecimal salePrice;

    private int stockLevel;

    private List<String> images;

    private boolean updated;

    private boolean isDiscontinued;

    private String arrivalDate;

    private String availablityMessage;

    public int compareTo(ArtisanProduct catalog){
        int compare = Comparator.comparing(ArtisanProduct::getSku)
                .thenComparing(ArtisanProduct::getProductName)
                .thenComparing(ArtisanProduct::getDescription)
                .thenComparing(ArtisanProduct::getPrice)
                .thenComparing(ArtisanProduct::getStockLevel)
                .compare(this, catalog);
        return compare;
    }
}
