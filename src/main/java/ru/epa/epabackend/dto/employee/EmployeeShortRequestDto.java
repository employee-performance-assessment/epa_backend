package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static ru.epa.epabackend.util.StringPatterns.CYRILLIC_LATIN_WHITESPACE_AND_DASH;
import static ru.epa.epabackend.util.StringPatterns.LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER;
import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс EmployeeShortRequestDto для передачи тела запроса на сервер с сокращенными данными для саморегистрации админа
 *
 * @author Валентина Вахламова
 */
@Builder
@Data
@AllArgsConstructor
public class EmployeeShortRequestDto {

    @NotBlank(groups = Create.class)
    @Size(min = 1, max = 255, groups = {Create.class, Update.class})
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH, groups = {Create.class, Update.class})
    protected String fullName;

    @NotBlank(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    @Size(min = 3, max = 255, groups = {Create.class, Update.class})
    protected String email;

    @NotEmpty(groups = Create.class)
    @Pattern(regexp = LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER, groups = {Create.class, Update.class})
    @Size(min = 8, max = 14, groups = {Create.class, Update.class})
    private String password;

    @Override
    public String toString() {
        return "Employee{" +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password={masked}" +
                '}';
    }
}
