package ru.epa.epabackend.util;

import java.time.format.DateTimeFormatter;

public final class DateConstant {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_SPACE = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private DateConstant() {
    }
}
