package com.ecomm.define.platforms.commons;

import com.ecomm.define.platforms.bigcommerce.ennum.Category;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class BCUtils {
    public static List<Integer> assignCategories(String title) {
        Set<Integer> categories = new HashSet<>();
        boolean categoryAssigned = false;
        for (Category category : Category.values()) {
            if (category.getCategoryWord().contains(" "))
            {
                if (title.toLowerCase().contains(category.getCategoryWord().toLowerCase())) {
                    categories.add(category.getCategoryCode());
                    categoryAssigned = true;
                }
            }
            for(String token:getTokensInTitle(title)){
                if (category.getCategoryWord().equalsIgnoreCase(token)) {
                    categories.add(category.getCategoryCode());
                    categoryAssigned = true;
                }
            }
        }
        if (!categoryAssigned)
        {
            categories.add(Category.FURNITURE.getCategoryCode());
        }
        return categories.parallelStream().collect(Collectors.toList());
    }

    private static List<String> getTokensInTitle(String str) {
        return Collections.list(new StringTokenizer(str, " ")).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }
}
