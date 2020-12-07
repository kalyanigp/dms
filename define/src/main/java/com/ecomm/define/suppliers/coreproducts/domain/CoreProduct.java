package com.ecomm.define.suppliers.coreproducts.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "coreProduct")
public class CoreProduct {

    @Id
    public ObjectId _id;
    @Indexed(unique = true)
    @CsvBindByName(column = "productcode")
    private String sku;

    @CsvBindByName(column = "collection")
    private String collection;

    @CsvBindByName(column = "range")
    private String range;

    @CsvBindByName(column = "category")
    private String category;

    @CsvBindByName(column = "barcode")
    private String ean;

    @CsvBindByName(column = "description")
    private String productName;

    @CsvBindByName(column = "External Product Colour 1")
    private String colour1;

    @CsvBindByName(column = "External Product Colour 2")
    private String colour2;

    @CsvBindByName(column = "Assembled Width (mm)")
    private String width;

    @CsvBindByName(column = "Assembled Depth (mm)")
    private String depth;

    @CsvBindByName(column = "Assembled Height (mm)")
    private String height;

    @CsvBindByName(column = "Prd wght (kgs)")
    private String weight;

    @CsvBindByName(column = "TRADE PRICE ()")
    private String tradePrice;

    @CsvBindByName(column = "Delivery Cost ()")
    private String deliveryCost;

    @CsvBindByName(column = "TOTAL DHD Cost ()")
    private String dhdCost;

    @CsvBindByName(column = "Construction")
    private String construction;

    @CsvBindByName(column = "Material 1")
    private String material1;

    @CsvBindByName(column = "Material 2")
    private String material2;

    @CsvBindByName(column = "Material 3")
    private String material3;

    @CsvBindByName(column = "Country of Manufacture")
    private String countryOfManuFacture;

    @CsvBindByName(column = "Romance Copy")
    private String description;

    @CsvBindByName(column = "Feature 1")
    private String feature1;

    @CsvBindByName(column = "Feature 2")
    private String feature2;

    @CsvBindByName(column = "Feature 3")
    private String feature3;

    @CsvBindByName(column = "Feature 4")
    private String feature4;

    @CsvBindByName(column = "Main Image")
    private String mainImage;

    @CsvBindByName(column = "360° Image Direct Link")
    private String mainImage360Degrees;

    @CsvBindByName(column = "Assembly Instructions")
    private String assemblyInstructions;



    private BigDecimal price;
    private BigDecimal salePrice;
    private int stockLevel;
    private List<String> images;
    private boolean updated;
    private boolean isDiscontinued;
    private String stockStatus;
    private String nextArrival;
    private String assembled;

    public void setImages(List<String> images) {
        List<String> imageList = new ArrayList<>();
        if(!StringUtils.isEmpty(getMainImage()) && getMainImage().contains("http")) {
            imageList.add(getMainImage());
        }
        this.images = imageList;
    }

    public int compareTo(CoreProduct catalog) {
        int compare = Comparator.comparing(CoreProduct::getSku)
                .thenComparing(CoreProduct::getProductName)
                .thenComparing(CoreProduct::getPrice)
                .thenComparing(CoreProduct::getStockLevel)
                .compare(this, catalog);
        return compare;
    }
}
