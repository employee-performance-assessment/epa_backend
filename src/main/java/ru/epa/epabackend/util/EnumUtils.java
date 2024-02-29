package ru.epa.epabackend.util;

public class EnumUtils {

    public static <T extends Enum<T>> T getEnum(Class<T> type, String name) {
        return Enum.valueOf(type, name.toUpperCase());
    }
}