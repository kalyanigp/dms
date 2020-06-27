package com.ecomm.define.config;

import com.ecomm.define.constants.bc.HeaderConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

public class HeadersConfig {

    public static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", HeaderConstants.ACCESS_TOKEN);
        headers.set("X-Auth-Client", HeaderConstants.CLIENT_ID);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }
}