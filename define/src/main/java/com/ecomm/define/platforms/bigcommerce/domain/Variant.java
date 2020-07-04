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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "product_id",
        "sku",
        "sku_id",
        "price",
        "calculated_price",
        "sale_price",
        "retail_price",
        "map_price",
        "weight",
        "width",
        "height",
        "depth",
        "is_free_shipping",
        "fixed_cost_shipping_price",
        "calculated_weight",
        "purchasing_disabled",
        "purchasing_disabled_message",
        "image_url",
        "cost_price",
        "upc",
        "mpn",
        "gtin",
        "inventory_level",
        "inventory_warning_level",
        "bin_picking_number",
        "option_values"
})
public class Variant {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("product_id")
    private Integer productId;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("sku_id")
    private Object skuId;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("calculated_price")
    private Integer calculatedPrice;
    @JsonProperty("sale_price")
    private Integer salePrice;
    @JsonProperty("retail_price")
    private Integer retailPrice;
    @JsonProperty("map_price")
    private Integer mapPrice;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("depth")
    private Integer depth;
    @JsonProperty("is_free_shipping")
    private Boolean isFreeShipping;
    @JsonProperty("fixed_cost_shipping_price")
    private Integer fixedCostShippingPrice;
    @JsonProperty("calculated_weight")
    private Integer calculatedWeight;
    @JsonProperty("purchasing_disabled")
    private Boolean purchasingDisabled;
    @JsonProperty("purchasing_disabled_message")
    private String purchasingDisabledMessage;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("cost_price")
    private Integer costPrice;
    @JsonProperty("upc")
    private String upc;
    @JsonProperty("mpn")
    private String mpn;
    @JsonProperty("gtin")
    private String gtin;
    @JsonProperty("inventory_level")
    private Integer inventoryLevel;
    @JsonProperty("inventory_warning_level")
    private Integer inventoryWarningLevel;
    @JsonProperty("bin_picking_number")
    private String binPickingNumber;
    @JsonProperty("option_values")
    private List<Object> optionValues = null;
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

    public Variant withId(Integer id) {
        this.id = id;
        return this;
    }

    @JsonProperty("product_id")
    public Integer getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Variant withProductId(Integer productId) {
        this.productId = productId;
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

    public Variant withSku(String sku) {
        this.sku = sku;
        return this;
    }

    @JsonProperty("sku_id")
    public Object getSkuId() {
        return skuId;
    }

    @JsonProperty("sku_id")
    public void setSkuId(Object skuId) {
        this.skuId = skuId;
    }

    public Variant withSkuId(Object skuId) {
        this.skuId = skuId;
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

    public Variant withPrice(Integer price) {
        this.price = price;
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

    public Variant withCalculatedPrice(Integer calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
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

    public Variant withSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
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

    public Variant withRetailPrice(Integer retailPrice) {
        this.retailPrice = retailPrice;
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

    public Variant withMapPrice(Integer mapPrice) {
        this.mapPrice = mapPrice;
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

    public Variant withWeight(Integer weight) {
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

    public Variant withWidth(Integer width) {
        this.width = width;
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

    public Variant withHeight(Integer height) {
        this.height = height;
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

    public Variant withDepth(Integer depth) {
        this.depth = depth;
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

    public Variant withIsFreeShipping(Boolean isFreeShipping) {
        this.isFreeShipping = isFreeShipping;
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

    public Variant withFixedCostShippingPrice(Integer fixedCostShippingPrice) {
        this.fixedCostShippingPrice = fixedCostShippingPrice;
        return this;
    }

    @JsonProperty("calculated_weight")
    public Integer getCalculatedWeight() {
        return calculatedWeight;
    }

    @JsonProperty("calculated_weight")
    public void setCalculatedWeight(Integer calculatedWeight) {
        this.calculatedWeight = calculatedWeight;
    }

    public Variant withCalculatedWeight(Integer calculatedWeight) {
        this.calculatedWeight = calculatedWeight;
        return this;
    }

    @JsonProperty("purchasing_disabled")
    public Boolean getPurchasingDisabled() {
        return purchasingDisabled;
    }

    @JsonProperty("purchasing_disabled")
    public void setPurchasingDisabled(Boolean purchasingDisabled) {
        this.purchasingDisabled = purchasingDisabled;
    }

    public Variant withPurchasingDisabled(Boolean purchasingDisabled) {
        this.purchasingDisabled = purchasingDisabled;
        return this;
    }

    @JsonProperty("purchasing_disabled_message")
    public String getPurchasingDisabledMessage() {
        return purchasingDisabledMessage;
    }

    @JsonProperty("purchasing_disabled_message")
    public void setPurchasingDisabledMessage(String purchasingDisabledMessage) {
        this.purchasingDisabledMessage = purchasingDisabledMessage;
    }

    public Variant withPurchasingDisabledMessage(String purchasingDisabledMessage) {
        this.purchasingDisabledMessage = purchasingDisabledMessage;
        return this;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Variant withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Variant withCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
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

    public Variant withUpc(String upc) {
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

    public Variant withMpn(String mpn) {
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

    public Variant withGtin(String gtin) {
        this.gtin = gtin;
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

    public Variant withInventoryLevel(Integer inventoryLevel) {
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

    public Variant withInventoryWarningLevel(Integer inventoryWarningLevel) {
        this.inventoryWarningLevel = inventoryWarningLevel;
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

    public Variant withBinPickingNumber(String binPickingNumber) {
        this.binPickingNumber = binPickingNumber;
        return this;
    }

    @JsonProperty("option_values")
    public List<Object> getOptionValues() {
        return optionValues;
    }

    @JsonProperty("option_values")
    public void setOptionValues(List<Object> optionValues) {
        this.optionValues = optionValues;
    }

    public Variant withOptionValues(List<Object> optionValues) {
        this.optionValues = optionValues;
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

    public Variant withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
