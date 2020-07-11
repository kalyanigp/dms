package com.ecomm.define.suppliers.artisan.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by vamshikirangullapelly on 06/07/2020.
 */
@AllArgsConstructor
@Data
public class ArtisanPrice {

    @Id
    public ObjectId _id;

    @Indexed(unique = true)
    @CsvBindByName(column = "FTG SKU No.")
    private String sku;

    @CsvBindByName(column = "HD Prices")
    private String price;

    public ArtisanPrice() {
    }
}
