package com.ecomm.define.bcenum;

/**
 * Created by vamshikirangullapelly on 04/05/2020.
 */
public enum Supplier {
    MAISON("Maison"),
    ARTISAN("Artisan"),
    FURNITURE2GO("Furniture 2 Go"),
    HILL_INTERIORS("Hill Interiors"),
    SELLER_BRAND("Define ");


    private String name;

    Supplier(String supplierName) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}