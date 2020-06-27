package com.ecomm.define.constants.bc;

import org.springframework.beans.factory.annotation.Value;

public class HeaderConstants {

    @Value("${bigcommerce.access.token}")
    public static String ACCESS_TOKEN;
    @Value("${bigcommerce.client.id}")
    public static String CLIENT_ID;
}
