package com.ecomm.define.domain.bigcommerce;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "url",
        "is_customized"
})
public class CustomUrl {

    @JsonProperty("url")
    private String url;
    @JsonProperty("is_customized")
    private Boolean isCustomized;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public CustomUrl withUrl(String url) {
        this.url = url;
        return this;
    }

    @JsonProperty("is_customized")
    public Boolean getIsCustomized() {
        return isCustomized;
    }

    @JsonProperty("is_customized")
    public void setIsCustomized(Boolean isCustomized) {
        this.isCustomized = isCustomized;
    }

    public CustomUrl withIsCustomized(Boolean isCustomized) {
        this.isCustomized = isCustomized;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public CustomUrl withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}