package com.ecomm.define.config;

import com.ecomm.define.constants.Constants;
import org.springframework.http.HttpHeaders;

public class HeadersConfig {

    public static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", Constants.ACCESS_TOKEN);
        headers.set("X-Auth-Client", Constants.CLIENT_ID);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }
}