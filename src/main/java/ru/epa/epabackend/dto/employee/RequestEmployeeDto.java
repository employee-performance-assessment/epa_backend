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
 * Класс для передачи тела запроса на сервер с полными данными для создания и обновления сотрудника
 *
 * @author Валентина Вахламова
 */
@Builder
@Data
@AllArgsConstructor
public class RequestEmployeeDto {

    @NotBlank(groups = {Create.class}, message = "Поле с фамилией и именем не должно быть пустым")
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH, groups = {Create.class, Update.class},
            message = "В фамилии и имени разрешены русские, английские символы, пробел и дефис")
    @Size(min = 1, max = 255, groups = {Create.class, Update.class}, message = "Допустимая длина фамилии и имени " +
            "от 1 до 255 символов")
    private String fullName;

    @NotBlank(groups = Create.class, message = "email не должен быть пустым")
    @Email(groups = {Create.class, Update.class}, message = "Поле email имеет некорректный формат")
    @Size(min = 3, max = 255, groups = {Create.class, Update.class}, message = "Допустимая длина поля email от 3 до 255 символов")
    private String email;

    @NotEmpty(groups = {Create.class}, message = "Пароль не должен быть пустым")
    @Pattern(regexp = LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER, groups = {Create.class, Update.class},
            message = "В поле пароль разрешены английские символы, цифры и спецсимволы ,:;?!*+%-<>@[]/_{}$#")
    @Size(min = 8, max = 14, groups = {Create.class, Update.class}, message = "Допустимая длина пароля от 8 до 14 " +
            "символов")
    private String password;

    @NotBlank(groups = {Create.class}, message = "Поле должность не должно быть пустым")
    @Size(min = 1, max = 255, groups = {Create.class, Update.class}, message = "Допустимая длина поля должность от 1" +
            " до 255 символов")
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH, groups = {Create.class, Update.class},
            message = "В поле должность разрешены русские, английские символы, пробел и дефис")
    private String position;

    @Override
    public String toString() {
        return "Employee{" +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password={masked}" +
                ", position=" + position +
                '}';
    }
}
