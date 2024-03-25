package ru.epa.epabackend.util;

import java.time.format.DateTimeFormatter;

/**
 * Утилитный класс DateConstant для форматирования даты и времени в json и обратно.
 *
 * @author Валентина Вахламова
 */
public final class DateConstant {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_SPACE = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final String TIME_PATTERN = "HH:mm:ss";

    private DateConstant() {
    }
}