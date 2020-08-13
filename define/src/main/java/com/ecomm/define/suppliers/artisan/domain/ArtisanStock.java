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
public class ArtisanStock {

    @Id
    public ObjectId _id;

    @Indexed(unique = true)
    @CsvBindByName(column = "sku")
    private String sku;

    @CsvBindByName(column = "stock_quantity")
    private int stockLevel;

    public ArtisanStock() {
    }
}
