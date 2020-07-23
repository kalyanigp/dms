package com.ecomm.define.suppliers.artisan.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@AllArgsConstructor
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


    @CsvBindByName(column = "ImageURL1")
    private String imageURL1;

    @CsvBindByName(column = "ImageURL2")
    private String imageURL2;

    @CsvBindByName(column = "ImageURL3")
    private String imageURL3;

    @CsvBindByName(column = "ImageURL4")
    private String imageURL4;

    @CsvBindByName(column = "ImageURL5")
    private String imageURL5;

    @CsvBindByName(column = "ImageURL6")
    private String imageURL6;

    @CsvBindByName(column = "ImageURL7")
    private String imageURL7;

    @CsvBindByName(column = "ImageURL8")
    private String imageURL8;

    @CsvBindByName(column = "ImageURL9")
    private String imageURL9;

    @CsvBindByName(column = "ImageURL10")
    private String imageURL10;

    @CsvBindByName(column = "ImageURL11")
    private String imageURL11;

    @CsvBindByName(column = "ImageURL12")
    private String imageURL12;

    @CsvBindByName(column = "ImageURL13")
    private String imageURL13;

    @CsvBindByName(column = "ImageURL14")
    private String imageURL14;

    @CsvBindByName(column = "HD Prices")
    private BigDecimal price;

    @CsvBindByName(column = "Qty On Hand")
    private int stockLevel;


    public ArtisanProduct() {
    }
}
