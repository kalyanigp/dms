package com.ecomm.define.suppliers.furniture2go.domain;

import com.ecomm.define.suppliers.artisan.domain.ArtisanProduct;
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
@Document(collection = "furniture2GoProduct")
public class Furniture2GoProduct {

    @Id
    public ObjectId _id;

    @Indexed(unique = true)
    @CsvBindByName(column = "SKU")
    private String sku;

    @CsvBindByName(column = "Product ID")
    private String mpn;

    @CsvBindByName(column = "Ranges")
    private String range;


    @CsvBindByName(column = "Product Name")
    private String productName;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "Finish")
    private String finish;

    @CsvBindByName(column = "EAN")
    private String ean;


    @CsvBindByName(column = "Width (mm)")
    private String width;


    @CsvBindByName(column = "Height (mm)")
    private String height;

    @CsvBindByName(column = "Depth (mm)")
    private String depth;

    @CsvBindByName(column = "Kg")
    private BigDecimal weight;

    @CsvBindByName(column = "Qty")
    private int noOfBoxes;

    @CsvBindByName(column = "BP 1")
    private String bp1;

    @CsvBindByName(column = "BP2")
    private String bp2;

    @CsvBindByName(column = "BP 3")
    private String bp3;

    @CsvBindByName(column = "BP 4")
    private String bp4;

    @CsvBindByName(column = "BP 5")
    private String bp5;

    @CsvBindByName(column = "BP 6")
    private String bp6;

    @CsvBindByName(column = "AssemblyInstructions")
    private String assemblyInstructions;

    private List<String> images;

    private BigDecimal hdPrice;

    private BigDecimal price;

    private int stockLevel;

    private boolean updated;

    private boolean isDiscontinued;

    private String stockArrivalDate;

    public int comparePrice(Furniture2GoProduct catalog){
        int compare = Comparator.comparing(Furniture2GoProduct::getSku)
                .thenComparing(Furniture2GoProduct::getProductName)
                .thenComparing(Furniture2GoProduct::getHdPrice)
                .compare(this, catalog);
        return compare;
    }

    public int compareStockLevel(Furniture2GoProduct catalog){
        int compare = Comparator.comparing(Furniture2GoProduct::getSku)
                .thenComparing(Furniture2GoProduct::getProductName)
                .thenComparing(Furniture2GoProduct::getStockLevel)
                .compare(this, catalog);
        return compare;
    }

}
