package ru.epa.epabackend.util;

public class StringPatterns {

    public static final String CYRILLIC_LATIN_ALPHABET_AND_NUMBERS = "^[a-zA-Zа-яА-ЯЁё0-9\\s\\-\\.\\,]+$";

    public static final String CYRILLIC_LATIN_WHITESPACE_AND_DASH = "^[а-яА-ЯЁёa-zA-Z\\s\\-]+$";

    public static final String LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER =
            "^[a-zA-Z0-9.,:;?!*+%\\-<>@\\[\\]{}/_$#]+$";

    public static final String CYRILLIC_LATIN_NUMBERS_SPECIAL_CHARACTERS =
            "^[a-zA-Zа-яА-ЯЁё0-9.,:;?!*+%\\-<>@\\[\\]{}/_$#]+$";

    public static final String TELEGRAM = "^@{1}[a-zA-Z0-9\\_]+$";

    private StringPatterns() {
    }
}