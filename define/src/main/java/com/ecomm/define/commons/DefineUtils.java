package com.ecomm.define.commons;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefineUtils {
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static BigDecimal percentage(BigDecimal base, BigDecimal pct) {
        return base.multiply(pct).divide(ONE_HUNDRED);
    }

    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
        return value1.multiply(value2);
    }

    public static BigDecimal getVat(BigDecimal base, BigDecimal pct) {
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

    public static boolean isValidDate(String d) {
        String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence) d);
        return matcher.matches();
    }

    public static Date plusDays(int days) {

        // Get current date
        Date currentDate = new Date();

        // convert date to localdatetime
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.of("Europe/London")).toLocalDateTime();

        // plus days
        localDateTime = localDateTime.plusDays(days);
        Date currentDatePlusOneDay = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        return currentDatePlusOneDay;
    }


}
