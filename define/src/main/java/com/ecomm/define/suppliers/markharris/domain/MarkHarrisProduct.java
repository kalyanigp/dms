package com.ecomm.define.suppliers.markharris.domain;

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
@Document(collection = "markHarrisProduct")
public class MarkHarrisProduct {

    @Id
    public ObjectId _id;
    @Indexed(unique = true)
    @CsvBindByName(column = "Supplier Part Number")
    private String sku;
    @CsvBindByName(column = "EAN Code")
    private String ean;
    @CsvBindByName(column = "Product Name")
    private String productName;
    @CsvBindByName(column = "Actual Product Weight")
    private String weight;
    @CsvBindByName(column = "Product Min Length/Depth")
    private String minLengthOrDepth;
    @CsvBindByName(column = "Product Max Length/Depth")
    private String maxLengthOrDepth;
    @CsvBindByName(column = "Product Min Width")
    private String minWidth;
    @CsvBindByName(column = "Product Max Width")
    private String maxWidth;
    @CsvBindByName(column = "Product Min Height")
    private String minHeight;
    @CsvBindByName(column = "Product Max Height")
    private String maxHeight;
    @CsvBindByName(column = "Paragraph description (if exist)")
    private String description;
    @CsvBindByName(column = "Assembly Required? (Y/N)")
    private String assemblyRequired;
    @CsvBindByName(column = "Distressed Finish? (Y/N)")
    private String distressedFinish;
    @CsvBindByName(column = "Ship Type ")
    private String shipType;
    @CsvBindByName(column = "Supplier Lead Time in Business Day Hours")
    private String supplierLeadTime;


    private String depth;
    private String width;
    private String height;
    private BigDecimal price;
    private int stockLevel;
    private List<String> images;
    private boolean updated;
    private boolean isDiscontinued;

    private String stockStatus;
    private String nextArrival;
    private String assembled;

    public int compareTo(MarkHarrisProduct catalog) {
        int compare = Comparator.comparing(MarkHarrisProduct::getSku)
                .thenComparing(MarkHarrisProduct::getProductName)
                .thenComparing(MarkHarrisProduct::getDescription)
                .thenComparing(MarkHarrisProduct::getPrice)
                .thenComparing(MarkHarrisProduct::getStockLevel)
                .compare(this, catalog);
        return compare;
    }
}
