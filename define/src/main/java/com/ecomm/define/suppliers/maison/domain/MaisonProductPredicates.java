package com.ecomm.define.suppliers.maison.domain;

import com.ecomm.define.suppliers.maison.domain.MaisonProduct;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Kalyani on 06/05/2020.
 */

public class MaisonProductPredicates {
    public static List<MaisonProduct> filterProducts(List<MaisonProduct> maisonProducts, Predicate<MaisonProduct> predicate) {
        return maisonProducts.stream().filter(predicate)
                .collect(Collectors.<MaisonProduct>toList());
    }

    public static Predicate<MaisonProduct> isPriceQuantityChanged( String productCode, String msp, int stockQty) {
        return maisonProduct -> (maisonProduct.getProductCode().equals(productCode)) && (!maisonProduct.getMspPrice().equals(msp) || maisonProduct.getStockQuantity() !=stockQty);
    }
}