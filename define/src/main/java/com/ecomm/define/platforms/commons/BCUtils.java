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
        for (Category category : Category.values()) {
            if((title.toLowerCase().contains("dining chair") || title.toLowerCase().contains("dining table")
                    || title.toLowerCase().contains("coffee table") || title.toLowerCase().contains("side table"))
                    || title.toLowerCase().contains("console table") || title.toLowerCase().contains("dressing table")
                    || title.toLowerCase().contains("sofa bed") || title.toLowerCase().contains("dining set")) {
                if (title.toLowerCase().contains(category.getCategoryWord().toLowerCase())) {
                    categories.add(category.getCategoryCode());
                }
            } else {
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
        }
        if (categories.size() == 0)
        {
            categories.add(Category.INTERIOR.getCategoryCode());
        }
        categories.add(Category.VIEW_ALL.getCategoryCode());
        return categories.parallelStream().collect(Collectors.toList());
    }

    private static List<String> getTokensinWord(String str) {
        return Collections.list(new StringTokenizer(str, " ")).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());
    }
}
