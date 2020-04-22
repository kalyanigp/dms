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

    @CsvBindByName(column = "Product Image ID - 2")
    private String productImageID_2;
    @CsvBindByName(column = "Product Image File - 2")
    private String productImageFile_2;
    @CsvBindByName(column = "Product Image Description - 2")
    private String productImageDescription_2;
    @CsvBindByName(column = "Product Image Is Thumbnail - 2")
    private String productImageIsThumbnail_2;
    @CsvBindByName(column = "Product Image Sort - 2")
    private String productImageSort_2;

    @CsvBindByName(column = "Product Image ID - 3")
    private String productImageID_3;
    @CsvBindByName(column = "Product Image File - 3")
    private String productImageFile_3;
    @CsvBindByName(column = "Product Image Description - 3")
    private String productImageDescription_3;
    @CsvBindByName(column = "Product Image Is Thumbnail - 3")
    private String productImageIsThumbnail_3;
    @CsvBindByName(column = "Product Image Sort - 3")
    private String productImageSort_3;

    @CsvBindByName(column = "Product Image ID - 4")
    private String productImageID_4;
    @CsvBindByName(column = "Product Image File - 4")
    private String productImageFile_4;
    @CsvBindByName(column = "Product Image Description - 4")
    private String productImageDescription_4;
    @CsvBindByName(column = "Product Image Is Thumbnail - 4")
    private String productImageIsThumbnail_4;
    @CsvBindByName(column = "Product Image Sort - 4")
    private String productImageSort_4;

    @CsvBindByName(column = "Product Image ID - 5")
    private String productImageID_5;
    @CsvBindByName(column = "Product Image File - 5")
    private String productImageFile_5;
    @CsvBindByName(column = "Product Image Description - 5")
    private String productImageDescription_5;
    @CsvBindByName(column = "Product Image Is Thumbnail - 5")
    private String productImageIsThumbnail_5;
    @CsvBindByName(column = "Product Image Sort - 5")
    private String productImageSort_5;

    @CsvBindByName(column = "Product Image ID - 6")
    private String productImageID_6;
    @CsvBindByName(column = "Product Image File - 6")
    private String productImageFile_6;
    @CsvBindByName(column = "Product Image Description - 6")
    private String productImageDescription_6;
    @CsvBindByName(column = "Product Image Is Thumbnail - 6")
    private String productImageIsThumbnail_6;
    @CsvBindByName(column = "Product Image Sort - 6")
    private String productImageSort_6;

    @CsvBindByName(column = "Product Image ID - 7")
    private String productImageID_7;
    @CsvBindByName(column = "Product Image File - 7")
    private String productImageFile_7;
    @CsvBindByName(column = "Product Image Description - 7")
    private String productImageDescription_7;
    @CsvBindByName(column = "Product Image Is Thumbnail - 7")
    private String productImageIsThumbnail_7;
    @CsvBindByName(column = "Product Image Sort - 7")
    private String productImageSort_7;

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

    public String getProductImageID_2() {
        return productImageID_2;
    }

    public String getProductImageFile_2() {
        return productImageFile_2;
    }

    public String getProductImageDescription_2() {
        return productImageDescription_2;
    }

    public String getProductImageIsThumbnail_2() {
        return productImageIsThumbnail_2;
    }

    public String getProductImageSort_2() {
        return productImageSort_2;
    }

    public String getProductImageID_3() {
        return productImageID_3;
    }

    public String getProductImageFile_3() {
        return productImageFile_3;
    }

    public String getProductImageDescription_3() {
        return productImageDescription_3;
    }

    public String getProductImageIsThumbnail_3() {
        return productImageIsThumbnail_3;
    }

    public String getProductImageSort_3() {
        return productImageSort_3;
    }

    public String getProductImageID_4() {
        return productImageID_4;
    }

    public String getProductImageFile_4() {
        return productImageFile_4;
    }

    public String getProductImageDescription_4() {
        return productImageDescription_4;
    }

    public String getProductImageIsThumbnail_4() {
        return productImageIsThumbnail_4;
    }

    public String getProductImageSort_4() {
        return productImageSort_4;
    }

    public String getProductImageID_5() {
        return productImageID_5;
    }

    public String getProductImageFile_5() {
        return productImageFile_5;
    }

    public String getProductImageDescription_5() {
        return productImageDescription_5;
    }

    public String getProductImageIsThumbnail_5() {
        return productImageIsThumbnail_5;
    }

    public String getProductImageSort_5() {
        return productImageSort_5;
    }

    public void setProductImageID_2(String productImageID_2) {
        this.productImageID_2 = productImageID_2;
    }

    public void setProductImageFile_2(String productImageFile_2) {
        this.productImageFile_2 = productImageFile_2;
    }

    public void setProductImageDescription_2(String productImageDescription_2) {
        this.productImageDescription_2 = productImageDescription_2;
    }

    public void setProductImageIsThumbnail_2(String productImageIsThumbnail_2) {
        this.productImageIsThumbnail_2 = productImageIsThumbnail_2;
    }

    public void setProductImageSort_2(String productImageSort_2) {
        this.productImageSort_2 = productImageSort_2;
    }

    public void setProductImageID_3(String productImageID_3) {
        this.productImageID_3 = productImageID_3;
    }

    public void setProductImageFile_3(String productImageFile_3) {
        this.productImageFile_3 = productImageFile_3;
    }

    public void setProductImageDescription_3(String productImageDescription_3) {
        this.productImageDescription_3 = productImageDescription_3;
    }

    public void setProductImageIsThumbnail_3(String productImageIsThumbnail_3) {
        this.productImageIsThumbnail_3 = productImageIsThumbnail_3;
    }

    public void setProductImageSort_3(String productImageSort_3) {
        this.productImageSort_3 = productImageSort_3;
    }

    public void setProductImageID_4(String productImageID_4) {
        this.productImageID_4 = productImageID_4;
    }

    public void setProductImageFile_4(String productImageFile_4) {
        this.productImageFile_4 = productImageFile_4;
    }

    public void setProductImageDescription_4(String productImageDescription_4) {
        this.productImageDescription_4 = productImageDescription_4;
    }

    public void setProductImageIsThumbnail_4(String productImageIsThumbnail_4) {
        this.productImageIsThumbnail_4 = productImageIsThumbnail_4;
    }

    public void setProductImageSort_4(String productImageSort_4) {
        this.productImageSort_4 = productImageSort_4;
    }

    public void setProductImageID_5(String productImageID_5) {
        this.productImageID_5 = productImageID_5;
    }

    public void setProductImageFile_5(String productImageFile_5) {
        this.productImageFile_5 = productImageFile_5;
    }

    public void setProductImageDescription_5(String productImageDescription_5) {
        this.productImageDescription_5 = productImageDescription_5;
    }

    public void setProductImageIsThumbnail_5(String productImageIsThumbnail_5) {
        this.productImageIsThumbnail_5 = productImageIsThumbnail_5;
    }

    public void setProductImageSort_5(String productImageSort_5) {
        this.productImageSort_5 = productImageSort_5;
    }

    public String getProductImageID_6() {
        return productImageID_6;
    }

    public void setProductImageID_6(String productImageID_6) {
        this.productImageID_6 = productImageID_6;
    }

    public String getProductImageFile_6() {
        return productImageFile_6;
    }

    public void setProductImageFile_6(String productImageFile_6) {
        this.productImageFile_6 = productImageFile_6;
    }

    public String getProductImageDescription_6() {
        return productImageDescription_6;
    }

    public void setProductImageDescription_6(String productImageDescription_6) {
        this.productImageDescription_6 = productImageDescription_6;
    }

    public String getProductImageIsThumbnail_6() {
        return productImageIsThumbnail_6;
    }

    public void setProductImageIsThumbnail_6(String productImageIsThumbnail_6) {
        this.productImageIsThumbnail_6 = productImageIsThumbnail_6;
    }

    public String getProductImageSort_6() {
        return productImageSort_6;
    }

    public void setProductImageSort_6(String productImageSort_6) {
        this.productImageSort_6 = productImageSort_6;
    }

    public String getProductImageID_7() {
        return productImageID_7;
    }

    public void setProductImageID_7(String productImageID_7) {
        this.productImageID_7 = productImageID_7;
    }

    public String getProductImageFile_7() {
        return productImageFile_7;
    }

    public void setProductImageFile_7(String productImageFile_7) {
        this.productImageFile_7 = productImageFile_7;
    }

    public String getProductImageDescription_7() {
        return productImageDescription_7;
    }

    public void setProductImageDescription_7(String productImageDescription_7) {
        this.productImageDescription_7 = productImageDescription_7;
    }

    public String getProductImageIsThumbnail_7() {
        return productImageIsThumbnail_7;
    }

    public void setProductImageIsThumbnail_7(String productImageIsThumbnail_7) {
        this.productImageIsThumbnail_7 = productImageIsThumbnail_7;
    }

    public String getProductImageSort_7() {
        return productImageSort_7;
    }

    public void setProductImageSort_7(String productImageSort_7) {
        this.productImageSort_7 = productImageSort_7;
    }
}
