package com.ke.bella.openapi.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    public static boolean isCurrentMonth(String month) {
        return getCurrentMonth().equals(month);
    }

    public static String getCurrentMonth() {
        return LocalDate.now().format(formatter);
    }

    public static long getCurrentMills() {
       return new Date().getTime();
    }
}
