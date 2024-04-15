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
public class RequestEmployeeShortDto {

    @NotBlank(groups = Create.class, message = "Поле с фамилией и именем не должно быть пустым")
    @Size(min = 1, max = 255, groups = {Create.class, Update.class}, message = "Допустимая длина фамилии и имени " +
            "от 1 до 255 символов")
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH, groups = {Create.class, Update.class},
            message = "В фамилии и имени разрешены русские, английские символы, пробел и дефис")
    protected String fullName;

    @NotBlank(groups = Create.class, message = "email не должен быть пустым")
    @Email(groups = {Create.class, Update.class}, message = "Поле email имеет некорректный формат")
    @Size(min = 3, max = 255, groups = {Create.class, Update.class}, message = "Допустимая длина поля email от 3 до " +
            "255 символов")
    protected String email;

    @NotEmpty(groups = Create.class, message = "Пароль не должен быть пустым")
    @Pattern(regexp = LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER, groups = {Create.class, Update.class},
            message = "В поле пароль разрешены английские символы, цифры и спецсимволы ,:;?!*+%-<>@[]/_{}$#")
    @Size(min = 8, max = 14, groups = {Create.class, Update.class}, message = "Допустимая длина пароля от 8 до 14 " +
            "символов")
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
