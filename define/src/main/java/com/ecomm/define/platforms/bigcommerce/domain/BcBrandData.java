package com.ecomm.define.platforms.bigcommerce.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "custom_url",
        "id",
        "image_url",
        "meta_description",
        "meta_keywords",
        "name",
        "page_title",
        "search_keywords"
})
@Document(collection="bcBrandData")
public class BcBrandData {
    @Id
    public ObjectId _id;
    @JsonProperty("custom_url")
    private CustomUrl customUrl;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("meta_description")
    private String metaDescription;
    @JsonProperty("meta_keywords")
    private List<String> metaKeywords = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("page_title")
    private String pageTitle;
    @JsonProperty("search_keywords")
    private String searchKeywords;



    @JsonProperty("custom_url")
    public CustomUrl getCustomUrl() {
        return customUrl;
    }

    @JsonProperty("custom_url")
    public void setCustomUrl(CustomUrl customUrl) {
        this.customUrl = customUrl;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("meta_description")
    public String getMetaDescription() {
        return metaDescription;
    }

    @JsonProperty("meta_description")
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    @JsonProperty("meta_keywords")
    public List<String> getMetaKeywords() {
        return metaKeywords;
    }

    @JsonProperty("meta_keywords")
    public void setMetaKeywords(List<String> metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("page_title")
    public String getPageTitle() {
        return pageTitle;
    }

    @JsonProperty("page_title")
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @JsonProperty("search_keywords")
    public String getSearchKeywords() {
        return searchKeywords;
    }

    @JsonProperty("search_keywords")
    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

}