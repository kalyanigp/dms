package com.ecomm.define.platforms.bigcommerce.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "type",
        "sku",
        "description",
        "weight",
        "width",
        "depth",
        "height",
        "price",
        "cost_price",
        "retail_price",
        "sale_price",
        "map_price",
        "tax_class_id",
        "product_tax_code",
        "calculated_price",
        "categories",
        "brand_id",
        "option_set_id",
        "option_set_display",
        "inventory_level",
        "inventory_warning_level",
        "inventory_tracking",
        "reviews_rating_sum",
        "reviews_count",
        "total_sold",
        "fixed_cost_shipping_price",
        "is_free_shipping",
        "is_visible",
        "is_featured",
        "related_products",
        "warranty",
        "bin_picking_number",
        "layout_file",
        "upc",
        "mpn",
        "gtin",
        "search_keywords",
        "availability",
        "availability_description",
        "gift_wrapping_options_type",
        "gift_wrapping_options_list",
        "sort_order",
        "condition",
        "is_condition_shown",
        "order_quantity_minimum",
        "order_quantity_maximum",
        "page_title",
        "meta_keywords",
        "meta_description",
        "date_created",
        "date_modified",
        "view_count",
        "preorder_release_date",
        "preorder_message",
        "is_preorder_only",
        "is_price_hidden",
        "price_hidden_label",
        "custom_url",
        "base_variant_id",
        "open_graph_type",
        "open_graph_title",
        "open_graph_description",
        "open_graph_use_meta_description",
        "open_graph_use_product_name",
        "open_graph_use_image",
        "variants",
        "images",
        "primary_image",
        "videos",
        "custom_fields",
        "bulk_pricing_rules",
        "reviews",
        "options",
        "modifiers",
        "parent_relations"
})
@Document(collection="bcProductData")
public class BcProductData {

    @Id
    public ObjectId _id;
    @JsonIgnore
    public String supplier;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @Indexed(unique = true)
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("description")
    private String description;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("depth")
    private Integer depth;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("cost_price")
    private Integer costPrice;
    @JsonProperty("retail_price")
    private Integer retailPrice;
    @JsonProperty("sale_price")
    private Integer salePrice;
    @JsonProperty("map_price")
    private Integer mapPrice;
    @JsonProperty("tax_class_id")
    private Integer taxClassId;
    @JsonProperty("product_tax_code")
    private String productTaxCode;
    @JsonProperty("calculated_price")
    private Integer calculatedPrice;
    @JsonProperty("categories")
    private List<Integer> categories = null;
    @JsonProperty("brand_id")
    private Integer brandId;
    @JsonProperty("option_set_id")
    private Object optionSetId;
    @JsonProperty("option_set_display")
    private String optionSetDisplay;
    @JsonProperty("inventory_level")
    private Integer inventoryLevel;
    @JsonProperty("inventory_warning_level")
    private Integer inventoryWarningLevel;
    @JsonProperty("inventory_tracking")
    private String inventoryTracking;
    @JsonProperty("reviews_rating_sum")
    private Integer reviewsRatingSum;
    @JsonProperty("reviews_count")
    private Integer reviewsCount;
    @JsonProperty("total_sold")
    private Integer totalSold;
    @JsonProperty("fixed_cost_shipping_price")
    private Integer fixedCostShippingPrice;
    @JsonProperty("is_free_shipping")
    private Boolean isFreeShipping;
    @JsonProperty("is_visible")
    private Boolean isVisible;
    @JsonProperty("is_featured")
    private Boolean isFeatured;
    @JsonProperty("related_products")
    private List<Integer> relatedProducts = null;
    @JsonProperty("warranty")
    private String warranty;
    @JsonProperty("bin_picking_number")
    private String binPickingNumber;
    @JsonProperty("layout_file")
    private String layoutFile;
    @JsonProperty("upc")
    private String upc;
    @JsonProperty("mpn")
    private String mpn;
    @JsonProperty("gtin")
    private String gtin;
    @JsonProperty("search_keywords")
    private String searchKeywords;
    @JsonProperty("availability")
    private String availability;
    @JsonProperty("availability_description")
    private String availabilityDescription;
    @JsonProperty("gift_wrapping_options_type")
    private String giftWrappingOptionsType;
    @JsonProperty("gift_wrapping_options_list")
    private List<Object> giftWrappingOptionsList = null;
    @JsonProperty("sort_order")
    private Integer sortOrder;
    @JsonProperty("condition")
    private String condition;
    @JsonProperty("is_condition_shown")
    private Boolean isConditionShown;
    @JsonProperty("order_quantity_minimum")
    private Integer orderQuantityMinimum;
    @JsonProperty("order_quantity_maximum")
    private Integer orderQuantityMaximum;
    @JsonProperty("page_title")
    private String pageTitle;
    @JsonProperty("meta_keywords")
    @JsonIgnore
    private List<Object> metaKeywords = null;
    @JsonProperty("meta_description")
    private String metaDescription;
    @JsonProperty("date_created")
    private String dateCreated;
    @JsonProperty("date_modified")
    private String dateModified;
    @JsonProperty("view_count")
    private Integer viewCount;
    @JsonProperty("preorder_release_date")
    private Object preorderReleaseDate;
    @JsonProperty("preorder_message")
    private String preorderMessage;
    @JsonProperty("is_preorder_only")
    private Boolean isPreorderOnly;
    @JsonProperty("is_price_hidden")
    private Boolean isPriceHidden;
    @JsonProperty("price_hidden_label")
    private String priceHiddenLabel;
    @JsonProperty("custom_url")
    private CustomUrl customUrl;
    @JsonProperty("base_variant_id")
    private Integer baseVariantId;
    @JsonProperty("open_graph_type")
    private String openGraphType;
    @JsonProperty("open_graph_title")
    private String openGraphTitle;
    @JsonProperty("open_graph_description")
    private String openGraphDescription;
    @JsonProperty("open_graph_use_meta_description")
    private Boolean openGraphUseMetaDescription;
    @JsonProperty("open_graph_use_product_name")
    private Boolean openGraphUseProductName;
    @JsonProperty("open_graph_use_image")
    private Boolean openGraphUseImage;
    @JsonProperty("variants")
    @JsonIgnore
    private List<Variant> variants = null;
    @JsonProperty("images")
    private List<Object> images = null;
    @JsonProperty("primary_image")
    private Object primaryImage;
    @JsonProperty("videos")
    private List<Object> videos = null;
    @JsonProperty("custom_fields")
    private List<Object> customFields = null;
    @JsonProperty("bulk_pricing_rules")
    private List<Object> bulkPricingRules = null;
    @JsonProperty("reviews")
    private List<Object> reviews = null;
    @JsonProperty("options")
    private List<Object> options = null;
    @JsonProperty("modifiers")
    private List<Object> modifiers = null;
    @JsonProperty("parent_relations")
    private List<Object> parentRelations = null;
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

    public BcProductData withId(Integer id) {
        this.id = id;
        return this;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public BcProductData withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public BcProductData withType(String type) {
        this.type = type;
        return this;
    }

    @JsonProperty("sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    public BcProductData withSku(String sku) {
        this.sku = sku;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public BcProductData withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public BcProductData withWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    public BcProductData withWidth(Integer width) {
        this.width = width;
        return this;
    }

    @JsonProperty("depth")
    public Integer getDepth() {
        return depth;
    }

    @JsonProperty("depth")
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public BcProductData withDepth(Integer depth) {
        this.depth = depth;
        return this;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
    }

    public BcProductData withHeight(Integer height) {
        this.height = height;
        return this;
    }

    @JsonProperty("price")
    public Integer getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Integer price) {
        this.price = price;
    }

    public BcProductData withPrice(Integer price) {
        this.price = price;
        return this;
    }

    @JsonProperty("cost_price")
    public Integer getCostPrice() {
        return costPrice;
    }

    @JsonProperty("cost_price")
    public void setCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
    }

    public BcProductData withCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
        return this;
    }

    @JsonProperty("retail_price")
    public Integer getRetailPrice() {
        return retailPrice;
    }

    @JsonProperty("retail_price")
    public void setRetailPrice(Integer retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BcProductData withRetailPrice(Integer retailPrice) {
        this.retailPrice = retailPrice;
        return this;
    }

    @JsonProperty("sale_price")
    public Integer getSalePrice() {
        return salePrice;
    }

    @JsonProperty("sale_price")
    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public BcProductData withSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
        return this;
    }

    @JsonProperty("map_price")
    public Integer getMapPrice() {
        return mapPrice;
    }

    @JsonProperty("map_price")
    public void setMapPrice(Integer mapPrice) {
        this.mapPrice = mapPrice;
    }

    public BcProductData withMapPrice(Integer mapPrice) {
        this.mapPrice = mapPrice;
        return this;
    }

    @JsonProperty("tax_class_id")
    public Integer getTaxClassId() {
        return taxClassId;
    }

    @JsonProperty("tax_class_id")
    public void setTaxClassId(Integer taxClassId) {
        this.taxClassId = taxClassId;
    }

    public BcProductData withTaxClassId(Integer taxClassId) {
        this.taxClassId = taxClassId;
        return this;
    }

    @JsonProperty("product_tax_code")
    public String getProductTaxCode() {
        return productTaxCode;
    }

    @JsonProperty("product_tax_code")
    public void setProductTaxCode(String productTaxCode) {
        this.productTaxCode = productTaxCode;
    }

    public BcProductData withProductTaxCode(String productTaxCode) {
        this.productTaxCode = productTaxCode;
        return this;
    }

    @JsonProperty("calculated_price")
    public Integer getCalculatedPrice() {
        return calculatedPrice;
    }

    @JsonProperty("calculated_price")
    public void setCalculatedPrice(Integer calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public BcProductData withCalculatedPrice(Integer calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
        return this;
    }

    @JsonProperty("categories")
    public List<Integer> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public BcProductData withCategories(List<Integer> categories) {
        this.categories = categories;
        return this;
    }

    @JsonProperty("brand_id")
    public Integer getBrandId() {
        return brandId;
    }

    @JsonProperty("brand_id")
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public BcProductData withBrandId(Integer brandId) {
        this.brandId = brandId;
        return this;
    }

    @JsonProperty("option_set_id")
    public Object getOptionSetId() {
        return optionSetId;
    }

    @JsonProperty("option_set_id")
    public void setOptionSetId(Object optionSetId) {
        this.optionSetId = optionSetId;
    }

    public BcProductData withOptionSetId(Object optionSetId) {
        this.optionSetId = optionSetId;
        return this;
    }

    @JsonProperty("option_set_display")
    public String getOptionSetDisplay() {
        return optionSetDisplay;
    }

    @JsonProperty("option_set_display")
    public void setOptionSetDisplay(String optionSetDisplay) {
        this.optionSetDisplay = optionSetDisplay;
    }

    public BcProductData withOptionSetDisplay(String optionSetDisplay) {
        this.optionSetDisplay = optionSetDisplay;
        return this;
    }

    @JsonProperty("inventory_level")
    public Integer getInventoryLevel() {
        return inventoryLevel;
    }

    @JsonProperty("inventory_level")
    public void setInventoryLevel(Integer inventoryLevel) {
        this.inventoryLevel = inventoryLevel;
    }

    public BcProductData withInventoryLevel(Integer inventoryLevel) {
        this.inventoryLevel = inventoryLevel;
        return this;
    }

    @JsonProperty("inventory_warning_level")
    public Integer getInventoryWarningLevel() {
        return inventoryWarningLevel;
    }

    @JsonProperty("inventory_warning_level")
    public void setInventoryWarningLevel(Integer inventoryWarningLevel) {
        this.inventoryWarningLevel = inventoryWarningLevel;
    }

    public BcProductData withInventoryWarningLevel(Integer inventoryWarningLevel) {
        this.inventoryWarningLevel = inventoryWarningLevel;
        return this;
    }

    @JsonProperty("inventory_tracking")
    public String getInventoryTracking() {
        return inventoryTracking;
    }

    @JsonProperty("inventory_tracking")
    public void setInventoryTracking(String inventoryTracking) {
        this.inventoryTracking = inventoryTracking;
    }

    public BcProductData withInventoryTracking(String inventoryTracking) {
        this.inventoryTracking = inventoryTracking;
        return this;
    }

    @JsonProperty("reviews_rating_sum")
    public Integer getReviewsRatingSum() {
        return reviewsRatingSum;
    }

    @JsonProperty("reviews_rating_sum")
    public void setReviewsRatingSum(Integer reviewsRatingSum) {
        this.reviewsRatingSum = reviewsRatingSum;
    }

    public BcProductData withReviewsRatingSum(Integer reviewsRatingSum) {
        this.reviewsRatingSum = reviewsRatingSum;
        return this;
    }

    @JsonProperty("reviews_count")
    public Integer getReviewsCount() {
        return reviewsCount;
    }

    @JsonProperty("reviews_count")
    public void setReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public BcProductData withReviewsCount(Integer reviewsCount) {
        this.reviewsCount = reviewsCount;
        return this;
    }

    @JsonProperty("total_sold")
    public Integer getTotalSold() {
        return totalSold;
    }

    @JsonProperty("total_sold")
    public void setTotalSold(Integer totalSold) {
        this.totalSold = totalSold;
    }

    public BcProductData withTotalSold(Integer totalSold) {
        this.totalSold = totalSold;
        return this;
    }

    @JsonProperty("fixed_cost_shipping_price")
    public Integer getFixedCostShippingPrice() {
        return fixedCostShippingPrice;
    }

    @JsonProperty("fixed_cost_shipping_price")
    public void setFixedCostShippingPrice(Integer fixedCostShippingPrice) {
        this.fixedCostShippingPrice = fixedCostShippingPrice;
    }

    public BcProductData withFixedCostShippingPrice(Integer fixedCostShippingPrice) {
        this.fixedCostShippingPrice = fixedCostShippingPrice;
        return this;
    }

    @JsonProperty("is_free_shipping")
    public Boolean getIsFreeShipping() {
        return isFreeShipping;
    }

    @JsonProperty("is_free_shipping")
    public void setIsFreeShipping(Boolean isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
    }

    public BcProductData withIsFreeShipping(Boolean isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
        return this;
    }

    @JsonProperty("is_visible")
    public Boolean getIsVisible() {
        return isVisible;
    }

    @JsonProperty("is_visible")
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public BcProductData withIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    @JsonProperty("is_featured")
    public Boolean getIsFeatured() {
        return isFeatured;
    }

    @JsonProperty("is_featured")
    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public BcProductData withIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
        return this;
    }

    @JsonProperty("related_products")
    public List<Integer> getRelatedProducts() {
        return relatedProducts;
    }

    @JsonProperty("related_products")
    public void setRelatedProducts(List<Integer> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public BcProductData withRelatedProducts(List<Integer> relatedProducts) {
        this.relatedProducts = relatedProducts;
        return this;
    }

    @JsonProperty("warranty")
    public String getWarranty() {
        return warranty;
    }

    @JsonProperty("warranty")
    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public BcProductData withWarranty(String warranty) {
        this.warranty = warranty;
        return this;
    }

    @JsonProperty("bin_picking_number")
    public String getBinPickingNumber() {
        return binPickingNumber;
    }

    @JsonProperty("bin_picking_number")
    public void setBinPickingNumber(String binPickingNumber) {
        this.binPickingNumber = binPickingNumber;
    }

    public BcProductData withBinPickingNumber(String binPickingNumber) {
        this.binPickingNumber = binPickingNumber;
        return this;
    }

    @JsonProperty("layout_file")
    public String getLayoutFile() {
        return layoutFile;
    }

    @JsonProperty("layout_file")
    public void setLayoutFile(String layoutFile) {
        this.layoutFile = layoutFile;
    }

    public BcProductData withLayoutFile(String layoutFile) {
        this.layoutFile = layoutFile;
        return this;
    }

    @JsonProperty("upc")
    public String getUpc() {
        return upc;
    }

    @JsonProperty("upc")
    public void setUpc(String upc) {
        this.upc = upc;
    }

    public BcProductData withUpc(String upc) {
        this.upc = upc;
        return this;
    }

    @JsonProperty("mpn")
    public String getMpn() {
        return mpn;
    }

    @JsonProperty("mpn")
    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public BcProductData withMpn(String mpn) {
        this.mpn = mpn;
        return this;
    }

    @JsonProperty("gtin")
    public String getGtin() {
        return gtin;
    }

    @JsonProperty("gtin")
    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public BcProductData withGtin(String gtin) {
        this.gtin = gtin;
        return this;
    }

    @JsonProperty("search_keywords")
    public String getSearchKeywords() {
        return searchKeywords;
    }

    @JsonProperty("search_keywords")
    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public BcProductData withSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
        return this;
    }

    @JsonProperty("availability")
    public String getAvailability() {
        return availability;
    }

    @JsonProperty("availability")
    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public BcProductData withAvailability(String availability) {
        this.availability = availability;
        return this;
    }

    @JsonProperty("availability_description")
    public String getAvailabilityDescription() {
        return availabilityDescription;
    }

    @JsonProperty("availability_description")
    public void setAvailabilityDescription(String availabilityDescription) {
        this.availabilityDescription = availabilityDescription;
    }

    public BcProductData withAvailabilityDescription(String availabilityDescription) {
        this.availabilityDescription = availabilityDescription;
        return this;
    }

    @JsonProperty("gift_wrapping_options_type")
    public String getGiftWrappingOptionsType() {
        return giftWrappingOptionsType;
    }

    @JsonProperty("gift_wrapping_options_type")
    public void setGiftWrappingOptionsType(String giftWrappingOptionsType) {
        this.giftWrappingOptionsType = giftWrappingOptionsType;
    }

    public BcProductData withGiftWrappingOptionsType(String giftWrappingOptionsType) {
        this.giftWrappingOptionsType = giftWrappingOptionsType;
        return this;
    }

    @JsonProperty("gift_wrapping_options_list")
    public List<Object> getGiftWrappingOptionsList() {
        return giftWrappingOptionsList;
    }

    @JsonProperty("gift_wrapping_options_list")
    public void setGiftWrappingOptionsList(List<Object> giftWrappingOptionsList) {
        this.giftWrappingOptionsList = giftWrappingOptionsList;
    }

    public BcProductData withGiftWrappingOptionsList(List<Object> giftWrappingOptionsList) {
        this.giftWrappingOptionsList = giftWrappingOptionsList;
        return this;
    }

    @JsonProperty("sort_order")
    public Integer getSortOrder() {
        return sortOrder;
    }

    @JsonProperty("sort_order")
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public BcProductData withSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    @JsonProperty("condition")
    public String getCondition() {
        return condition;
    }

    @JsonProperty("condition")
    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BcProductData withCondition(String condition) {
        this.condition = condition;
        return this;
    }

    @JsonProperty("is_condition_shown")
    public Boolean getIsConditionShown() {
        return isConditionShown;
    }

    @JsonProperty("is_condition_shown")
    public void setIsConditionShown(Boolean isConditionShown) {
        this.isConditionShown = isConditionShown;
    }

    public BcProductData withIsConditionShown(Boolean isConditionShown) {
        this.isConditionShown = isConditionShown;
        return this;
    }

    @JsonProperty("order_quantity_minimum")
    public Integer getOrderQuantityMinimum() {
        return orderQuantityMinimum;
    }

    @JsonProperty("order_quantity_minimum")
    public void setOrderQuantityMinimum(Integer orderQuantityMinimum) {
        this.orderQuantityMinimum = orderQuantityMinimum;
    }

    public BcProductData withOrderQuantityMinimum(Integer orderQuantityMinimum) {
        this.orderQuantityMinimum = orderQuantityMinimum;
        return this;
    }

    @JsonProperty("order_quantity_maximum")
    public Integer getOrderQuantityMaximum() {
        return orderQuantityMaximum;
    }

    @JsonProperty("order_quantity_maximum")
    public void setOrderQuantityMaximum(Integer orderQuantityMaximum) {
        this.orderQuantityMaximum = orderQuantityMaximum;
    }

    public BcProductData withOrderQuantityMaximum(Integer orderQuantityMaximum) {
        this.orderQuantityMaximum = orderQuantityMaximum;
        return this;
    }

    @JsonProperty("page_title")
    public String getPageTitle() {
        return pageTitle;
    }

    @JsonProperty("page_title")
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public BcProductData withPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
        return this;
    }

    @JsonProperty("meta_keywords")
    public List<Object> getMetaKeywords() {
        return metaKeywords;
    }

    @JsonProperty("meta_keywords")
    public void setMetaKeywords(List<Object> metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public BcProductData withMetaKeywords(List<Object> metaKeywords) {
        this.metaKeywords = metaKeywords;
        return this;
    }

    @JsonProperty("meta_description")
    public String getMetaDescription() {
        return metaDescription;
    }

    @JsonProperty("meta_description")
    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public BcProductData withMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
        return this;
    }

    @JsonProperty("date_created")
    public String getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("date_created")
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public BcProductData withDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @JsonProperty("date_modified")
    public String getDateModified() {
        return dateModified;
    }

    @JsonProperty("date_modified")
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public BcProductData withDateModified(String dateModified) {
        this.dateModified = dateModified;
        return this;
    }

    @JsonProperty("view_count")
    public Integer getViewCount() {
        return viewCount;
    }

    @JsonProperty("view_count")
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public BcProductData withViewCount(Integer viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    @JsonProperty("preorder_release_date")
    public Object getPreorderReleaseDate() {
        return preorderReleaseDate;
    }

    @JsonProperty("preorder_release_date")
    public void setPreorderReleaseDate(Object preorderReleaseDate) {
        this.preorderReleaseDate = preorderReleaseDate;
    }

    public BcProductData withPreorderReleaseDate(Object preorderReleaseDate) {
        this.preorderReleaseDate = preorderReleaseDate;
        return this;
    }

    @JsonProperty("preorder_message")
    public String getPreorderMessage() {
        return preorderMessage;
    }

    @JsonProperty("preorder_message")
    public void setPreorderMessage(String preorderMessage) {
        this.preorderMessage = preorderMessage;
    }

    public BcProductData withPreorderMessage(String preorderMessage) {
        this.preorderMessage = preorderMessage;
        return this;
    }

    @JsonProperty("is_preorder_only")
    public Boolean getIsPreorderOnly() {
        return isPreorderOnly;
    }

    @JsonProperty("is_preorder_only")
    public void setIsPreorderOnly(Boolean isPreorderOnly) {
        this.isPreorderOnly = isPreorderOnly;
    }

    public BcProductData withIsPreorderOnly(Boolean isPreorderOnly) {
        this.isPreorderOnly = isPreorderOnly;
        return this;
    }

    @JsonProperty("is_price_hidden")
    public Boolean getIsPriceHidden() {
        return isPriceHidden;
    }

    @JsonProperty("is_price_hidden")
    public void setIsPriceHidden(Boolean isPriceHidden) {
        this.isPriceHidden = isPriceHidden;
    }

    public BcProductData withIsPriceHidden(Boolean isPriceHidden) {
        this.isPriceHidden = isPriceHidden;
        return this;
    }

    @JsonProperty("price_hidden_label")
    public String getPriceHiddenLabel() {
        return priceHiddenLabel;
    }

    @JsonProperty("price_hidden_label")
    public void setPriceHiddenLabel(String priceHiddenLabel) {
        this.priceHiddenLabel = priceHiddenLabel;
    }

    public BcProductData withPriceHiddenLabel(String priceHiddenLabel) {
        this.priceHiddenLabel = priceHiddenLabel;
        return this;
    }

    @JsonProperty("custom_url")
    public CustomUrl getCustomUrl() {
        return customUrl;
    }

    @JsonProperty("custom_url")
    public void setCustomUrl(CustomUrl customUrl) {
        this.customUrl = customUrl;
    }

    public BcProductData withCustomUrl(CustomUrl customUrl) {
        this.customUrl = customUrl;
        return this;
    }

    @JsonProperty("base_variant_id")
    public Integer getBaseVariantId() {
        return baseVariantId;
    }

    @JsonProperty("base_variant_id")
    public void setBaseVariantId(Integer baseVariantId) {
        this.baseVariantId = baseVariantId;
    }

    public BcProductData withBaseVariantId(Integer baseVariantId) {
        this.baseVariantId = baseVariantId;
        return this;
    }

    @JsonProperty("open_graph_type")
    public String getOpenGraphType() {
        return openGraphType;
    }

    @JsonProperty("open_graph_type")
    public void setOpenGraphType(String openGraphType) {
        this.openGraphType = openGraphType;
    }

    public BcProductData withOpenGraphType(String openGraphType) {
        this.openGraphType = openGraphType;
        return this;
    }

    @JsonProperty("open_graph_title")
    public String getOpenGraphTitle() {
        return openGraphTitle;
    }

    @JsonProperty("open_graph_title")
    public void setOpenGraphTitle(String openGraphTitle) {
        this.openGraphTitle = openGraphTitle;
    }

    public BcProductData withOpenGraphTitle(String openGraphTitle) {
        this.openGraphTitle = openGraphTitle;
        return this;
    }

    @JsonProperty("open_graph_description")
    public String getOpenGraphDescription() {
        return openGraphDescription;
    }

    @JsonProperty("open_graph_description")
    public void setOpenGraphDescription(String openGraphDescription) {
        this.openGraphDescription = openGraphDescription;
    }

    public BcProductData withOpenGraphDescription(String openGraphDescription) {
        this.openGraphDescription = openGraphDescription;
        return this;
    }

    @JsonProperty("open_graph_use_meta_description")
    public Boolean getOpenGraphUseMetaDescription() {
        return openGraphUseMetaDescription;
    }

    @JsonProperty("open_graph_use_meta_description")
    public void setOpenGraphUseMetaDescription(Boolean openGraphUseMetaDescription) {
        this.openGraphUseMetaDescription = openGraphUseMetaDescription;
    }

    public BcProductData withOpenGraphUseMetaDescription(Boolean openGraphUseMetaDescription) {
        this.openGraphUseMetaDescription = openGraphUseMetaDescription;
        return this;
    }

    @JsonProperty("open_graph_use_product_name")
    public Boolean getOpenGraphUseProductName() {
        return openGraphUseProductName;
    }

    @JsonProperty("open_graph_use_product_name")
    public void setOpenGraphUseProductName(Boolean openGraphUseProductName) {
        this.openGraphUseProductName = openGraphUseProductName;
    }

    public BcProductData withOpenGraphUseProductName(Boolean openGraphUseProductName) {
        this.openGraphUseProductName = openGraphUseProductName;
        return this;
    }

    @JsonProperty("open_graph_use_image")
    public Boolean getOpenGraphUseImage() {
        return openGraphUseImage;
    }

    @JsonProperty("open_graph_use_image")
    public void setOpenGraphUseImage(Boolean openGraphUseImage) {
        this.openGraphUseImage = openGraphUseImage;
    }

    public BcProductData withOpenGraphUseImage(Boolean openGraphUseImage) {
        this.openGraphUseImage = openGraphUseImage;
        return this;
    }

    @JsonProperty("variants")
    public List<Variant> getVariants() {
        return variants;
    }

    @JsonProperty("variants")
    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public BcProductData withVariants(List<Variant> variants) {
        this.variants = variants;
        return this;
    }

    @JsonProperty("images")
    public List<Object> getImages() {
        return images;
    }

    @JsonProperty("images")
    public void setImages(List<Object> images) {
        this.images = images;
    }

    public BcProductData withImages(List<Object> images) {
        this.images = images;
        return this;
    }

    @JsonProperty("primary_image")
    public Object getPrimaryImage() {
        return primaryImage;
    }

    @JsonProperty("primary_image")
    public void setPrimaryImage(Object primaryImage) {
        this.primaryImage = primaryImage;
    }

    public BcProductData withPrimaryImage(Object primaryImage) {
        this.primaryImage = primaryImage;
        return this;
    }

    @JsonProperty("videos")
    public List<Object> getVideos() {
        return videos;
    }

    @JsonProperty("videos")
    public void setVideos(List<Object> videos) {
        this.videos = videos;
    }

    public BcProductData withVideos(List<Object> videos) {
        this.videos = videos;
        return this;
    }

    @JsonProperty("custom_fields")
    public List<Object> getCustomFields() {
        return customFields;
    }

    @JsonProperty("custom_fields")
    public void setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
    }

    public BcProductData withCustomFields(List<Object> customFields) {
        this.customFields = customFields;
        return this;
    }

    @JsonProperty("bulk_pricing_rules")
    public List<Object> getBulkPricingRules() {
        return bulkPricingRules;
    }

    @JsonProperty("bulk_pricing_rules")
    public void setBulkPricingRules(List<Object> bulkPricingRules) {
        this.bulkPricingRules = bulkPricingRules;
    }

    public BcProductData withBulkPricingRules(List<Object> bulkPricingRules) {
        this.bulkPricingRules = bulkPricingRules;
        return this;
    }

    @JsonProperty("reviews")
    public List<Object> getReviews() {
        return reviews;
    }

    @JsonProperty("reviews")
    public void setReviews(List<Object> reviews) {
        this.reviews = reviews;
    }

    public BcProductData withReviews(List<Object> reviews) {
        this.reviews = reviews;
        return this;
    }

    @JsonProperty("options")
    public List<Object> getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(List<Object> options) {
        this.options = options;
    }

    public BcProductData withOptions(List<Object> options) {
        this.options = options;
        return this;
    }

    @JsonProperty("modifiers")
    public List<Object> getModifiers() {
        return modifiers;
    }

    @JsonProperty("modifiers")
    public void setModifiers(List<Object> modifiers) {
        this.modifiers = modifiers;
    }

    public BcProductData withModifiers(List<Object> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    @JsonProperty("parent_relations")
    public List<Object> getParentRelations() {
        return parentRelations;
    }

    @JsonProperty("parent_relations")
    public void setParentRelations(List<Object> parentRelations) {
        this.parentRelations = parentRelations;
    }

    public BcProductData withParentRelations(List<Object> parentRelations) {
        this.parentRelations = parentRelations;
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

    public BcProductData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
