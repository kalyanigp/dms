package com.ecomm.define.suppliers.markharris.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarkHarrisImage {
    @CsvBindByName(column = "SKU")
    private String sku;

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

}
