package com.ecomm.define.suppliers.artisan.domain;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Kalyani on 06/05/2020.
 */

public class ArtisanPredicates {
    public static List<Furniture2GoProduct> filterProducts(List<Furniture2GoProduct> furniture2GoProducts, Predicate<Furniture2GoProduct> predicate) {
        return furniture2GoProducts.stream().filter(predicate)
                .collect(Collectors.<Furniture2GoProduct>toList());
    }

    /*public static Predicate<Furniture2GoProduct> isPriceQuantityChanged(String productCode, String msp, int stockQty) {
        return furniture2Go -> (furniture2Go.getProductCode().equals(productCode)) && (!furniture2Go.getMspPrice().equals(msp) || furniture2Go.getStockQuantity() !=stockQty);
    }*/
}