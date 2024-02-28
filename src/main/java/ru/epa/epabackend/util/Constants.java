package ru.epa.epabackend.util;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String DATE_TIME_STRING = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_SPACE = DateTimeFormatter.ofPattern(DATE_TIME_STRING);
}
