package com.ecomm.define.domain.bigcommerce;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "parent_id",
        "name",
        "description",
        "views",
        "sort_order",
        "page_title",
        "meta_keywords",
        "meta_description",
        "layout_file",
        "image_url",
        "is_visible",
        "search_keywords",
        "default_product_sort",
        "custom_url"
})
@Document(collection="bcCategoryData")
public class BcCategoryData {
    @Id
    public ObjectId _id;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("parent_id")
    private Integer parentId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("views")
    private Integer views;
    @JsonProperty("sort_order")
    private Integer sortOrder;
    @JsonProperty("page_title")
    private String pageTitle;
    @JsonProperty("meta_keywords")
    private List<String> metaKeywords = null;
    @JsonProperty("meta_description")
    private String metaDescription;
    @JsonProperty("layout_file")
    private String layoutFile;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("is_visible")
    private Boolean isVisible;
    @JsonProperty("search_keywords")
    private String searchKeywords;
    @JsonProperty("default_product_sort")
    private String defaultProductSort;
    @JsonProperty("custom_url")
    private CustomUrl customUrl;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("parent_id")
    public Integer getParentId() {
        return parentId;
    }

    @JsonProperty("parent_id")
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("views")
    public Integer getViews() {
        return views;
    }

    @JsonProperty("views")
    public void setViews(Integer views) {
        this.views = views;
    }

    @JsonProperty("sort_order")
    public Integer getSortOrder() {
        return sortOrder;
    }

    @JsonProperty("sort_order")
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @JsonProperty("page_title")
    public String getPageTitle() {
        return pageTitle;
    }

    @JsonProperty("page_title")
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @JsonProperty("meta_keywords")
    public List<String> getMetaKeywords() {
        return metaKeywords;
    }

    @JsonProperty("meta_keywords")
    public void setMetaKeywords(List<String> metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    @JsonProperty("meta_description")
    public String getMetaDescription() {
        return metaDescription;
    }

    @JsonProperty("meta_description")
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    @JsonProperty("layout_file")
    public String getLayoutFile() {
        return layoutFile;
    }

    @JsonProperty("layout_file")
    public void setLayoutFile(String layoutFile) {
        this.layoutFile = layoutFile;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("is_visible")
    public Boolean getIsVisible() {
        return isVisible;
    }

    @JsonProperty("is_visible")
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    @JsonProperty("search_keywords")
    public String getSearchKeywords() {
        return searchKeywords;
    }

    @JsonProperty("search_keywords")
    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    @JsonProperty("default_product_sort")
    public String getDefaultProductSort() {
        return defaultProductSort;
    }

    @JsonProperty("default_product_sort")
    public void setDefaultProductSort(String defaultProductSort) {
        this.defaultProductSort = defaultProductSort;
    }

    @JsonProperty("custom_url")
    public CustomUrl getCustomUrl() {
        return customUrl;
    }

    @JsonProperty("custom_url")
    public void setCustomUrl(CustomUrl customUrl) {
        this.customUrl = customUrl;
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
