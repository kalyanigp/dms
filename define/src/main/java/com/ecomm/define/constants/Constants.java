package com.ecomm.define.constants;

import org.springframework.beans.factory.annotation.Value;

public class Constants {

    @Value("${bigcommerce.access.token}")
    public static String ACCESS_TOKEN;
    @Value("${bigcommerce.client.id}")
    public static String CLIENT_ID;

    public static String MAISON_CODE = "MREP";
}
