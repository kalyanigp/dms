package com.ecomm.define.suppliers.maison.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@AllArgsConstructor
@Data
@Document(collection = "maisonProduct")
public class MaisonProduct {

    @Id
    public ObjectId _id;

    @CsvBindByName(column = "Title")
    private String title;
    @Indexed(unique = true)
    @CsvBindByName(column = "Product Code")
    private String productCode;
    @CsvBindByName(column = "Trade Price")
    private String tradePrice;
    @CsvBindByName(column = "MSP Price")
    private String mspPrice;
    @CsvBindByName(column = "Stock Quantity")
    private int stockQuantity;
    @CsvBindByName(column = "Size")
    private String size;
    @CsvBindByName(column = "Material")
    private String material;
    @CsvBindByName(column = "EAN")
    private String ean;
    @CsvBindByName(column = "Packing Specs")
    private String packingSpec;
    @CsvBindByName(column = "Images")
    private String images;
    private boolean isUpdated;
    private boolean isDiscontinued;

    public MaisonProduct() {
    }


    public int compareTo(MaisonProduct catalog) {
        int compare = Comparator.comparing(MaisonProduct::getProductCode)
                .thenComparing(MaisonProduct::getTitle)
                .thenComparing(MaisonProduct::getTradePrice)
                .thenComparing(MaisonProduct::getStockQuantity)
                .thenComparing(MaisonProduct::getImages)
                .compare(this, catalog);
        return compare;
    }
}
