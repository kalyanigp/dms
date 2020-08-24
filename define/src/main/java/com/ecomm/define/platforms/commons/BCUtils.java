package com.ecomm.define.platforms.commons;

import com.ecomm.define.platforms.bigcommerce.constants.BcConstants;
import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.ennum.Category;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class BCUtils {
    public static List<Integer> assignCategories(String title) {
        Set<Integer> categories = new HashSet<>();
        int dualCat = getDualWordCategory(title.toLowerCase());
        if (dualCat == BigDecimal.ZERO.intValue()) {
            for (Category category : Category.values()) {
                if (category.getCategoryWord().contains(" ")) {
                    for (String token : getTokensinWord(category.getCategoryWord())) {
                        if (title.contains(token)) {
                            categories.add(category.getCategoryCode());
                        }
                    }
                }
                for (String token : getTokensinWord(title)) {
                    if (category.getCategoryWord().equalsIgnoreCase(token)) {
                        categories.add(category.getCategoryCode());
                    }
                }
            }
        } else {
            categories.add(dualCat);
        }
        if (categories.isEmpty())
        {
            categories.add(Category.INTERIOR.getCategoryCode());
        }
        categories.add(Category.VIEW_ALL.getCategoryCode());
        //Temp Fix
        if (categories.contains(Category.LIVING_SOFAS.getCategoryCode()) || categories.contains(Category.LIVING_ARMCHAIRS.getCategoryCode()) || categories.contains(Category.LIVING_SOFA_BEDS.getCategoryCode()) || categories.contains(Category.LIVING_FOOTSTOOLS.getCategoryCode()) || categories.contains(Category.MEDIA_TABLE.getCategoryCode()) || categories.contains(Category.LIVING_STORAGE.getCategoryCode()) ||
                categories.contains(Category.SA_SOFAS.getCategoryCode()) || categories.contains(Category.SOFAS_ARMCHAIRS.getCategoryCode()) || categories.contains(Category.SA_SOFAS.getCategoryCode()) || categories.contains(Category.SA_ARMCHAIRS.getCategoryCode()) || categories.contains(Category.SA_FOOTSTOOLS.getCategoryCode())) {
            categories.add(Category.LIVING.getCategoryCode());
        }
        return categories.parallelStream().collect(Collectors.toList());
    }

    private static List<String> getTokensinWord(String str) {
        return Collections.list(new StringTokenizer(str, " ")).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }


    public static void setInventoryParameters(int stockLevel, BcProductData byProductSku) {
        if (stockLevel > BigDecimal.ZERO.intValue()) {
            byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING);
            byProductSku.setAvailability(BcConstants.AVAILABLE);
        } else {
            byProductSku.setInventoryTracking(BcConstants.INVENTORY_TRACKING_NONE);
            byProductSku.setAvailability(BcConstants.PREORDER);
            byProductSku.setIsPreorderOnly(true);
        }
    }

    private static int getDualWordCategory(String title) {
        int category = 0;
        if (title.contains("dining chair")) {
            category = Category.KD_DINING_CHAIRS.getCategoryCode();
        } else if (title.contains("dining table")) {
            category = Category.KD_DINING_TABLES.getCategoryCode();
        } else if (title.contains("coffee table")) {
            category = Category.LIVING_TABLES_COFFEE_TABLES.getCategoryCode();
        } else if (title.contains("side table") || title.contains("end table") || title.contains("sidetable")) {
            category = Category.SIDE_TABLE.getCategoryCode();

        } else if (title.contains("console table")) {
            category = Category.HALLWAY_CONSOLE_TABLES.getCategoryCode();

        } else if (title.contains("dressing table")) {
            category = Category.DRESSING_TABLE.getCategoryCode();

        } else if (title.contains("sofa bed")) {
            category = Category.SA_SOFABEDS.getCategoryCode();

        } else if (title.contains("dining set")) {
            category = Category.KD_DINING_TABLE_SETS.getCategoryCode();

        } else if (title.contains("table lamp")) {
            category = Category.LAMP_SHADE.getCategoryCode();

        }

        return category;
    }
}
