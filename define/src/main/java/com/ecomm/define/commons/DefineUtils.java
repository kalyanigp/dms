package com.ecomm.define.commons;

import java.math.BigDecimal;

public class DefineUtils {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static BigDecimal percentage(BigDecimal base, BigDecimal pct){
        return base.multiply(pct).divide(ONE_HUNDRED);
    }

    public static BigDecimal getVat(BigDecimal base, BigDecimal pct){
        return base.multiply(pct).divide(ONE_HUNDRED);
    }


}
