package com.ecomm.define.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@AllArgsConstructor
@Getter
@Setter
@Document(collection="maisonProduct")
public class MaisonProduct {

    @Id
    public ObjectId _id;

    @CsvBindByName(column = "Title")
    private String title;
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

    public MaisonProduct() {
    }
}
