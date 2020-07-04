package com.ecomm.define.platforms.bigcommerce.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "product_id",
        "is_thumbnail",
        "sort_order",
        "description",
        "image_file",
        "url_zoom",
        "url_standard",
        "url_thumbnail",
        "url_tiny",
        "date_modified"
})
@Document(collection="bcProductImageData")
public class BcProductImageData {
    @Id
    public ObjectId _id;

    @Indexed(unique = true)
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("product_id")
    private Integer productId;
    @JsonProperty("is_thumbnail")
    private Boolean isThumbnail;
    @JsonProperty("sort_order")
    private Integer sortOrder;
    @JsonProperty("description")
    private String description;
    @JsonProperty("image_file")
    private String imageFile;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("url_zoom")
    private String urlZoom;
    @JsonProperty("url_standard")
    private String urlStandard;
    @JsonProperty("url_thumbnail")
    private String urlThumbnail;
    @JsonProperty("url_tiny")
    private String urlTiny;
    @JsonProperty("date_modified")
    private String dateModified;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("product_id")
    public Integer getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @JsonProperty("is_thumbnail")
    public Boolean getIsThumbnail() {
        return isThumbnail;
    }

    @JsonProperty("is_thumbnail")
    public void setIsThumbnail(Boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }

    @JsonProperty("sort_order")
    public Integer getSortOrder() {
        return sortOrder;
    }

    @JsonProperty("sort_order")
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("image_file")
    public String getImageFile() {
        return imageFile;
    }

    @JsonProperty("image_file")
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("url_zoom")
    public String getUrlZoom() {
        return urlZoom;
    }

    @JsonProperty("url_zoom")
    public void setUrlZoom(String urlZoom) {
        this.urlZoom = urlZoom;
    }

    @JsonProperty("url_standard")
    public String getUrlStandard() {
        return urlStandard;
    }

    @JsonProperty("url_standard")
    public void setUrlStandard(String urlStandard) {
        this.urlStandard = urlStandard;
    }

    @JsonProperty("url_thumbnail")
    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    @JsonProperty("url_thumbnail")
    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    @JsonProperty("url_tiny")
    public String getUrlTiny() {
        return urlTiny;
    }

    @JsonProperty("url_tiny")
    public void setUrlTiny(String urlTiny) {
        this.urlTiny = urlTiny;
    }

    @JsonProperty("date_modified")
    public String getDateModified() {
        return dateModified;
    }

    @JsonProperty("date_modified")
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

