package com.ecomm.define.suppliers.lpdfurniture.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by vamshikirangullapelly on 18/04/2020.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "lpdProduct")
public class LpdProduct {

    @Id
    public ObjectId _id;

    @Indexed(unique = true)
    @CsvBindByName(column = "sku")
    private String sku;

    @CsvBindByName(column = "name")
    private String productName;

    @CsvBindByName(column = "F_BathroomType")
    private String bathroomType;

    @CsvBindByName(column = "F_BedType")
    private String bedType;

    @CsvBindByName(column = "F_BedFurnitureType")
    private String bedFurnitureType;

    @CsvBindByName(column = "F_DiningType")
    private String diningType;

    @CsvBindByName(column = "F_LivingType")
    private String livingType;

    @CsvBindByName(column = "F_SofaSeatingType")
    private String sofaSeatingType;

    @CsvBindByName(column = "F_officeType")
    private String officeType;

    @CsvBindByName(column = "F_Material")
    private String finish;

    @CsvBindByName(column = "F_Colour")
    private String colour;

    @CsvBindByName(column = "F_Style")
    private String style;

    @CsvBindByName(column = "f_assemblytype")
    private String assemblyType;

    @CsvBindByName(column = "F_NumberofDoors")
    private String numberOfDoors;

    @CsvBindByName(column = "F_MountType")
    private String mountType;

    @CsvBindByName(column = "F_NumberofShelves")
    private String numberOfShelves;

    @CsvBindByName(column = "F_BedDesign")
    private String bedDesign;

    @CsvBindByName(column = "f_mattresstype")
    private String mattressType;

    @CsvBindByName(column = "F_HangingRail")
    private String hangingRail;

    @CsvBindByName(column = "F_TableType")
    private String tableType;

    @CsvBindByName(column = "F_Seats")
    private String seats;

    @CsvBindByName(column = "F_NumberofPeople")
    private String numberOfPeople;

    @CsvBindByName(column = "F_SofaBeds")
    private String sofaBeds;

    @CsvBindByName(column = "F_Family")
    private String family;

    @CsvBindByName(column = "AssembledWidth")
    private BigDecimal width;

    @CsvBindByName(column = "AssembledHeight")
    private BigDecimal height;

    @CsvBindByName(column = "AssembledDepth")
    private BigDecimal depth;

    @CsvBindByName(column = "EAN Code")
    private String ean;

    @CsvBindByName(column = "Number of Boxes")
    private Integer numberOfBoxes;

    @CsvBindByName(column = "Box Sizes")
    private String boxSizes;

    @CsvBindByName(column = "boxesWeght")
    private String boxesWeight;

    private BigDecimal weight;

    @CsvBindByName(column = "DHD Price")
    private BigDecimal dhdPrice;

    @CsvBindByName(column = "P_I_Product_Information")
    private String description;

    @CsvBindByName(column = "image_name_url")
    private String image1;

    @CsvBindByName(column = "image_name2_url")
    private String image2;

    @CsvBindByName(column = "image_name3_url")
    private String image3;

    @CsvBindByName(column = "image_name4_url")
    private String image4;

    @CsvBindByName(column = "image_name_lifestyle_url")
    private String image5;

    private List<String> images = new ArrayList<>();

    private BigDecimal price;

    private BigDecimal salePrice;

    private Integer stockLevel;

    private boolean updated;

    private boolean isDiscontinued;

    public List<String> getImages() {
        images.add(getImage1());
        images.add(getImage2());
        images.add(getImage3());
        images.add(getImage4());
        images.add(getImage5());
        return images;
    }

    public BigDecimal getWeight() {
        String boxesWeight = getBoxesWeight().trim();
        if (!boxesWeight.isEmpty()) {
            String[] boxesWeightSplit = boxesWeight.split("\\|");
            int weightSum = 0;
            for (int i = 0; i < boxesWeightSplit.length; i++) {
                weightSum += Integer.parseInt(boxesWeightSplit[i].trim());
            }
            this.weight = new BigDecimal(weightSum);
        }
        return weight;
    }

    public int compareTo(LpdProduct catalog) {
        int compare = Comparator.comparing(LpdProduct::getSku)
                .thenComparing(LpdProduct::getProductName)
                .thenComparing(LpdProduct::getPrice)
                .thenComparing(LpdProduct::getStockLevel)
                .compare(this, catalog);
        return compare;
    }
}
