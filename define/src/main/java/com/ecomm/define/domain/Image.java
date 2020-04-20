package com.ecomm.define.domain;

import com.opencsv.bean.CsvBindByName;
import org.springframework.data.annotation.Id;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
public class Image {

    @Id
    public String _id;

    @CsvBindByName(column = "Images")
    private String imageLinks;

}
