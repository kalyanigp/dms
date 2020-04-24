package com.ecomm.define.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */

@AllArgsConstructor
@Data
@Document(collection="bcProduct")
public class BigCommerceProduct {

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
    @CsvBindByName(column = "Product File - 2")
    private String productFile_2;
    @CsvBindByName(column = "Product Image Description - 2")
    private String productImageDescription_2;
    @CsvBindByName(column = "Product Image Is Thumbnail - 2")
    private String productImageIsThumbnail_2;
    @CsvBindByName(column = "Product Image Sort - 2")
    private String productImageSort_2;
    @CsvBindByName(column = "Product Image File - 2")
    private String productImageFile_2;

    @CsvBindByName(column = "Product Image ID - 3")
    private String productImageID_3;
    @CsvBindByName(column = "Product File - 3")
    private String productFile_3;
    @CsvBindByName(column = "Product Image Description - 3")
    private String productImageDescription_3;
    @CsvBindByName(column = "Product Image Is Thumbnail - 3")
    private String productImageIsThumbnail_3;
    @CsvBindByName(column = "Product Image Sort - 3")
    private String productImageSort_3;
    @CsvBindByName(column = "Product Image File - 3")
    private String productImageFile_3;

    @CsvBindByName(column = "Product Image ID - 4")
    private String productImageID_4;
    @CsvBindByName(column = "Product File - 4")
    private String productFile_4;
    @CsvBindByName(column = "Product Image Description - 4")
    private String productImageDescription_4;
    @CsvBindByName(column = "Product Image Is Thumbnail - 4")
    private String productImageIsThumbnail_4;
    @CsvBindByName(column = "Product Image Sort - 4")
    private String productImageSort_4;
    @CsvBindByName(column = "Product Image File - 4")
    private String productImageFile_4;

    @CsvBindByName(column = "Product Image ID - 5")
    private String productImageID_5;
    @CsvBindByName(column = "Product File - 5")
    private String productFile_5;
    @CsvBindByName(column = "Product Image Description - 5")
    private String productImageDescription_5;
    @CsvBindByName(column = "Product Image Is Thumbnail - 5")
    private String productImageIsThumbnail_5;
    @CsvBindByName(column = "Product Image Sort - 5")
    private String productImageSort_5;
    @CsvBindByName(column = "Product Image File - 5")
    private String productImageFile_5;

    @CsvBindByName(column = "Product Image ID - 6")
    private String productImageID_6;
    @CsvBindByName(column = "Product File - 6")
    private String productFile_6;
    @CsvBindByName(column = "Product Image Description - 6")
    private String productImageDescription_6;
    @CsvBindByName(column = "Product Image Is Thumbnail - 6")
    private String productImageIsThumbnail_6;
    @CsvBindByName(column = "Product Image Sort - 6")
    private String productImageSort_6;
    @CsvBindByName(column = "Product Image File - 6")
    private String productImageFile_6;

    @CsvBindByName(column = "Product Image ID - 7")
    private String productImageID_7;
    @CsvBindByName(column = "Product File - 7")
    private String productFile_7;
    @CsvBindByName(column = "Product Image Description - 7")
    private String productImageDescription_7;
    @CsvBindByName(column = "Product Image Is Thumbnail - 7")
    private String productImageIsThumbnail_7;
    @CsvBindByName(column = "Product Image Sort - 7")
    private String productImageSort_7;
    @CsvBindByName(column = "Product Image File - 7")
    private String productImageFile_7;

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


    public BigCommerceProduct() {
    }

}
