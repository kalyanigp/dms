package com.ecomm.define.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class HeadersConfig {
    @Value("${bigcommerce.access.token}")
    private String ACCESS_TOKEN;
    @Value("${bigcommerce.client.id}")
    private String CLIENT_ID;
    @Value("${bigcommerce.storehash}")
    private String storeHash;
    @Value("${bigcommerce.client.baseUrl}")
    private String baseUrl;

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", ACCESS_TOKEN);
        headers.set("X-Auth-Client", CLIENT_ID);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        return headers;
    }

    public String getStoreHash() {
        return storeHash;
    }
    public String getBaseUrl() {
        return baseUrl;
    }
}