package com.ecomm.define.suppliers.markharris.domain;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by vamshikirangullapelly on 06/07/2020.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarkHarrisPrice {

    @Id
    public ObjectId _id;

    @CsvBindByPosition(position = 0)
    private String sku;

    @CsvBindByPosition(position = 3)
    private String price;

   /* @CsvBindByName(column = "RANGE")
    private String range;

    @CsvBindByName(column = "LENGTH")
    private String length;

    @CsvBindByName(column = "WIDTH")
    private String width;

    @CsvBindByName(column = "HEIGHT")
    private String height;


    @CsvBindByName(column = "ASSEMBLED")
    private String assembled; */

}
