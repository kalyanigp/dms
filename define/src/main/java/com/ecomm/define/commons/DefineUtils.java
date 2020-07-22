package com.ecomm.define.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static String getDelimiter(String value) {
        String delimiter = "";
        if (value.contains("/")) {
            delimiter = "/";
        } else if (value.contains("-")) {
            delimiter = "-";
        } else if (value.contains(",")) {
            delimiter = ",";
        }
        return delimiter;
    }

    public static List<String> evaluateDimensions(String delimeter, String value) {
        List<String> resultValues = new ArrayList<>();
        switch (delimeter) {
            case "/":
                resultValues = Arrays.asList(value.split("/"));
                break;
            case "-":
                resultValues = Arrays.asList(value.split("-"));
                break;
            case ",":
                resultValues = Arrays.asList(value.split(","));
                break;
        }
        return resultValues;
    }


}
