package com.ecomm.define.platforms.commons;

import com.ecomm.define.platforms.bigcommerce.domain.BcProductData;
import com.ecomm.define.platforms.bigcommerce.ennum.Category;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BCUtils {
    public static List<Integer> assignCategories(String title) {
        Set<Integer> categories = new HashSet<>();
        categories.add(Category.FURNITURE.getCategoryCode());
        for (Category category : Category.values()) {
            if (title.toLowerCase().contains(category.getCategoryWord().toLowerCase())) {
                categories.add(category.getCategoryCode());
            }
        }
        return categories.parallelStream().collect(Collectors.toList());
    }
}
