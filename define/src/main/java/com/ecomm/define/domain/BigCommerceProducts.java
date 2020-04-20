package com.ecomm.define.domain;

import com.opencsv.bean.CsvBindByName;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@Document(collection="bcProduct")
public class BigCommerceProducts {

    @Id
    public ObjectId _id;

    @CsvBindByName(column = "Product Code/SKU")
    private String productCode;
    @CsvBindByName(column = "Product Name")
    private String title;
    @CsvBindByName(column = "Price")
    private String mspPrice;
    @CsvBindByName(column = "Cost Price")
    private String tradePrice;
    @CsvBindByName(column = "Category")
    private String category;
    @CsvBindByName(column = "Allow Purchases?")
    private String allowPurchases;
    @CsvBindByName(column = "Product Weight")
    private String productWeight;
    @CsvBindByName(column = "Product Width")
    private String productWidth;
    @CsvBindByName(column = "Product Height")
    private String productHeight;
    @CsvBindByName(column = "Product Depth")
    private String productDepth;
    @CsvBindByName(column = "Track Inventory")
    private String trackInventory;

    @CsvBindByName(column = "Product ID")
    private String productID;
    @CsvBindByName(column = "Item Type")
    private String itemType;
    @CsvBindByName(column = "Product Type")
    private String productType;
    @CsvBindByName(column = "Bin Picking Number")
    private String binPickingNumber;
    @CsvBindByName(column = "Brand Name")
    private String brandName;
    @CsvBindByName(column = "Option Set")
    private String optionSet;
    @CsvBindByName(column = "Option Set Align")
    private String optionSetAlign;
    @CsvBindByName(column = "Product Description")
    private String productDescription;
    @CsvBindByName(column = "Retail Price")
    private String retailPrice;
    @CsvBindByName(column = "Sale Price")
    private String salePrice;
    @CsvBindByName(column = "Fixed Shipping Cost")
    private String fixedShippingCost;
    @CsvBindByName(column = "")
    private String freeShipping;
    @CsvBindByName(column = "Product Warranty")
    private String productWarranty;
    @CsvBindByName(column = "Product Visible?")
    private String productVisible;
    @CsvBindByName(column = "Product Availability")
    private String productAvailability;
    @CsvBindByName(column = "Current Stock Level")
    private String currentStockLevel;
    @CsvBindByName(column = "Low Stock Level")
    private String lowStockLevel;
    @CsvBindByName(column = "Product File - 1")
    private String productFile_1;
    @CsvBindByName(column = "Product File Description - 1")
    private String productFileDescription_1;
    @CsvBindByName(column = "Product File Max Downloads - 1")
    private String productFileMaxDownloads_1;
    @CsvBindByName(column = "Product File Expires After - 1")
    private String productFileExpiresAfter_1;
    @CsvBindByName(column = "Product Image ID - 1")
    private String productImageID_1;
    @CsvBindByName(column = "Product Image File - 1")
    private String productImageFile_1;
    @CsvBindByName(column = "Product Image Description - 1")
    private String productImageDescription_1;
    @CsvBindByName(column = "Product Image Is Thumbnail - 1")
    private String productImageIsThumbnail_1;
    @CsvBindByName(column = "Product Image Sort - 1")
    private String productImageSort_1;
    @CsvBindByName(column = "Search Keywords")
    private String searchKeywords;
    @CsvBindByName(column = "Page Title")
    private String pageTitle;
    @CsvBindByName(column = "Meta Keywords")
    private String metaKeywords;
    @CsvBindByName(column = "Meta Description")
    private String metaDescription;
    @CsvBindByName(column = "MYOB Asset Acct")
    private String myOBAssetAcct;
    @CsvBindByName(column = "MYOB Income Acct")
    private String myOBIncomeAcct;
    @CsvBindByName(column = "MYOB Expense Acct")
    private String myOBExpenseAcct;
    @CsvBindByName(column = "Product Condition")
    private String productCondition;
    @CsvBindByName(column = "Show Product Condition?")
    private String showProductCondition;
    @CsvBindByName(column = "Sort Order")
    private String sortOrder;
    @CsvBindByName(column = "Product Tax Class")
    private String productTaxClass;
    @CsvBindByName(column = "Product UPC/EAN")
    private String productUPC_EAN;
    @CsvBindByName(column = "Stop Processing Rules")
    private String stopProcessingRules;
    @CsvBindByName(column = "Product URL")
    private String productURL;
    @CsvBindByName(column = "Redirect Old URL?")
    private String redirectOldURL;
    @CsvBindByName(column = "GPS Global Trade Item Number")
    private String gPSGlobalTradeItemNumber;
    @CsvBindByName(column = "GPS Manufacturer Part Number")
    private String gPSManufacturerPartNumber;
    @CsvBindByName(column = "GPS Gender")
    private String gPSGender;
    @CsvBindByName(column = "GPS Age Group")
    private String gPSAgeGroup;
    @CsvBindByName(column = "GPS Color")
    private String gPSColor;
    @CsvBindByName(column = "GPS Size")
    private String gPSSize;
    @CsvBindByName(column = "GPS Material")
    private String gPSMaterial;
    @CsvBindByName(column = "GPS Pattern")
    private String gPSPattern;
    @CsvBindByName(column = "GPS Item Group ID")
    private String gPSItemGroupID;
    @CsvBindByName(column = "GPS Category")
    private String gPSCategory;
    @CsvBindByName(column = "GPS Enabled")
    private String gPSEnabled;
    @CsvBindByName(column = "Tax Provider Tax Code")
    private String taxProviderTaxCode;
    @CsvBindByName(column = "Product Custom Fields")
    private String productCustomFields;


    public BigCommerceProducts() {

    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMspPrice() {
        return mspPrice;
    }

    public void setMspPrice(String mspPrice) {
        this.mspPrice = mspPrice;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getBinPickingNumber() {
        return binPickingNumber;
    }

    public void setBinPickingNumber(String binPickingNumber) {
        this.binPickingNumber = binPickingNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getOptionSet() {
        return optionSet;
    }

    public void setOptionSet(String optionSet) {
        this.optionSet = optionSet;
    }

    public String getOptionSetAlign() {
        return optionSetAlign;
    }

    public void setOptionSetAlign(String optionSetAlign) {
        this.optionSetAlign = optionSetAlign;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getFixedShippingCost() {
        return fixedShippingCost;
    }

    public void setFixedShippingCost(String fixedShippingCost) {
        this.fixedShippingCost = fixedShippingCost;
    }

    public String getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(String freeShipping) {
        this.freeShipping = freeShipping;
    }

    public String getProductWarranty() {
        return productWarranty;
    }

    public void setProductWarranty(String productWarranty) {
        this.productWarranty = productWarranty;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(String productWidth) {
        this.productWidth = productWidth;
    }

    public String getProductHeight() {
        return productHeight;
    }

    public void setProductHeight(String productHeight) {
        this.productHeight = productHeight;
    }

    public String getProductDepth() {
        return productDepth;
    }

    public void setProductDepth(String productDepth) {
        this.productDepth = productDepth;
    }

    public String getAllowPurchases() {
        return allowPurchases;
    }

    public void setAllowPurchases(String allowPurchases) {
        this.allowPurchases = allowPurchases;
    }

    public String getProductVisible() {
        return productVisible;
    }

    public void setProductVisible(String productVisible) {
        this.productVisible = productVisible;
    }

    public String getProductAvailability() {
        return productAvailability;
    }

    public void setProductAvailability(String productAvailability) {
        this.productAvailability = productAvailability;
    }

    public String getTrackInventory() {
        return trackInventory;
    }

    public void setTrackInventory(String trackInventory) {
        this.trackInventory = trackInventory;
    }

    public String getCurrentStockLevel() {
        return currentStockLevel;
    }

    public void setCurrentStockLevel(String currentStockLevel) {
        this.currentStockLevel = currentStockLevel;
    }

    public String getLowStockLevel() {
        return lowStockLevel;
    }

    public void setLowStockLevel(String lowStockLevel) {
        this.lowStockLevel = lowStockLevel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductFile_1() {
        return productFile_1;
    }

    public void setProductFile_1(String productFile_1) {
        this.productFile_1 = productFile_1;
    }

    public String getProductFileDescription_1() {
        return productFileDescription_1;
    }

    public void setProductFileDescription_1(String productFileDescription_1) {
        this.productFileDescription_1 = productFileDescription_1;
    }

    public String getProductFileMaxDownloads_1() {
        return productFileMaxDownloads_1;
    }

    public void setProductFileMaxDownloads_1(String productFileMaxDownloads_1) {
        this.productFileMaxDownloads_1 = productFileMaxDownloads_1;
    }

    public String getProductFileExpiresAfter_1() {
        return productFileExpiresAfter_1;
    }

    public void setProductFileExpiresAfter_1(String productFileExpiresAfter_1) {
        this.productFileExpiresAfter_1 = productFileExpiresAfter_1;
    }

    public String getProductImageID_1() {
        return productImageID_1;
    }

    public void setProductImageID_1(String productImageID_1) {
        this.productImageID_1 = productImageID_1;
    }

    public String getProductImageFile_1() {
        return productImageFile_1;
    }

    public void setProductImageFile_1(String productImageFile_1) {
        this.productImageFile_1 = productImageFile_1;
    }

    public String getProductImageDescription_1() {
        return productImageDescription_1;
    }

    public void setProductImageDescription_1(String productImageDescription_1) {
        this.productImageDescription_1 = productImageDescription_1;
    }

    public String getProductImageIsThumbnail_1() {
        return productImageIsThumbnail_1;
    }

    public void setProductImageIsThumbnail_1(String productImageIsThumbnail_1) {
        this.productImageIsThumbnail_1 = productImageIsThumbnail_1;
    }

    public String getProductImageSort_1() {
        return productImageSort_1;
    }

    public void setProductImageSort_1(String productImageSort_1) {
        this.productImageSort_1 = productImageSort_1;
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }



    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getShowProductCondition() {
        return showProductCondition;
    }

    public void setShowProductCondition(String showProductCondition) {
        this.showProductCondition = showProductCondition;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getProductTaxClass() {
        return productTaxClass;
    }

    public void setProductTaxClass(String productTaxClass) {
        this.productTaxClass = productTaxClass;
    }


    public String getStopProcessingRules() {
        return stopProcessingRules;
    }

    public void setStopProcessingRules(String stopProcessingRules) {
        this.stopProcessingRules = stopProcessingRules;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getRedirectOldURL() {
        return redirectOldURL;
    }

    public void setRedirectOldURL(String redirectOldURL) {
        this.redirectOldURL = redirectOldURL;
    }

    public String getgPSGlobalTradeItemNumber() {
        return gPSGlobalTradeItemNumber;
    }

    public void setgPSGlobalTradeItemNumber(String gPSGlobalTradeItemNumber) {
        this.gPSGlobalTradeItemNumber = gPSGlobalTradeItemNumber;
    }

    public String getgPSManufacturerPartNumber() {
        return gPSManufacturerPartNumber;
    }

    public void setgPSManufacturerPartNumber(String gPSManufacturerPartNumber) {
        this.gPSManufacturerPartNumber = gPSManufacturerPartNumber;
    }

    public String getgPSGender() {
        return gPSGender;
    }

    public void setgPSGender(String gPSGender) {
        this.gPSGender = gPSGender;
    }

    public String getgPSAgeGroup() {
        return gPSAgeGroup;
    }

    public void setgPSAgeGroup(String gPSAgeGroup) {
        this.gPSAgeGroup = gPSAgeGroup;
    }

    public String getgPSColor() {
        return gPSColor;
    }

    public void setgPSColor(String gPSColor) {
        this.gPSColor = gPSColor;
    }

    public String getgPSSize() {
        return gPSSize;
    }

    public void setgPSSize(String gPSSize) {
        this.gPSSize = gPSSize;
    }

    public String getgPSMaterial() {
        return gPSMaterial;
    }

    public void setgPSMaterial(String gPSMaterial) {
        this.gPSMaterial = gPSMaterial;
    }

    public String getgPSPattern() {
        return gPSPattern;
    }

    public void setgPSPattern(String gPSPattern) {
        this.gPSPattern = gPSPattern;
    }

    public String getgPSItemGroupID() {
        return gPSItemGroupID;
    }

    public void setgPSItemGroupID(String gPSItemGroupID) {
        this.gPSItemGroupID = gPSItemGroupID;
    }

    public String getgPSCategory() {
        return gPSCategory;
    }

    public void setgPSCategory(String gPSCategory) {
        this.gPSCategory = gPSCategory;
    }

    public String getgPSEnabled() {
        return gPSEnabled;
    }

    public void setgPSEnabled(String gPSEnabled) {
        this.gPSEnabled = gPSEnabled;
    }

    public String getTaxProviderTaxCode() {
        return taxProviderTaxCode;
    }

    public void setTaxProviderTaxCode(String taxProviderTaxCode) {
        this.taxProviderTaxCode = taxProviderTaxCode;
    }

    public String getProductCustomFields() {
        return productCustomFields;
    }

    public void setProductCustomFields(String productCustomFields) {
        this.productCustomFields = productCustomFields;
    }

    public String getMyOBAssetAcct() {
        return myOBAssetAcct;
    }

    public void setMyOBAssetAcct(String myOBAssetAcct) {
        this.myOBAssetAcct = myOBAssetAcct;
    }

    public String getMyOBIncomeAcct() {
        return myOBIncomeAcct;
    }

    public void setMyOBIncomeAcct(String myOBIncomeAcct) {
        this.myOBIncomeAcct = myOBIncomeAcct;
    }

    public String getMyOBExpenseAcct() {
        return myOBExpenseAcct;
    }

    public void setMyOBExpenseAcct(String myOBExpenseAcct) {
        this.myOBExpenseAcct = myOBExpenseAcct;
    }

    public String getProductUPC_EAN() {
        return productUPC_EAN;
    }

    public void setProductUPC_EAN(String productUPC_EAN) {
        this.productUPC_EAN = productUPC_EAN;
    }
}
