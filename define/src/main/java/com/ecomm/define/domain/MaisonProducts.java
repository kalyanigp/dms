package com.ecomm.define.domain;

import com.opencsv.bean.CsvBindByName;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@Document(collection="maisonProduct")
public class MaisonProducts {

    @Id
    public ObjectId _id;


    @CsvBindByName(column = "Title")
    private String title;
    @CsvBindByName(column = "Product Code")
    private String productCode;
    @CsvBindByName(column = "Trade Price")
    private String tradePrice;
    @CsvBindByName(column = "MSP Price")
    private String mspPrice;
    @CsvBindByName(column = "Stock Quantity")
    private int stockQuantity;
    @CsvBindByName(column = "Size")
    private String size;
    @CsvBindByName(column = "Material")
    private String material;
    @CsvBindByName(column = "EAN")
    private String ean;
    @CsvBindByName(column = "Packing Specs")
    private String packingSpec;
    /*@CsvBindByName(column = "Images")
    private List<Image> imageLinks;*/
    @CsvBindByName(column = "Images")
    private String images;
    //private String


    public MaisonProducts() {

    }

    public MaisonProducts(String title, String productCode, String tradePrice, String mspPrice, int stockQuantity, String size, String material, String ean, String packingSpec, String images) {
        this.title = title;
        this.productCode = productCode;
        this.tradePrice = tradePrice;
        this.mspPrice = mspPrice;
        this.stockQuantity = stockQuantity;
        this.size = size;
        this.material = material;
        this.ean = ean;
        this.packingSpec = packingSpec;
        this.images = images;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getMspPrice() {
        return mspPrice;
    }

    public void setMspPrice(String mspPrice) {
        this.mspPrice = mspPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getPackingSpec() {
        return packingSpec;
    }

    public void setPackingSpec(String packingSpec) {
        this.packingSpec = packingSpec;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
