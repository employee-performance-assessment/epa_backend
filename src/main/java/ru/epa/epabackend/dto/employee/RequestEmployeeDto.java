package ru.epa.epabackend.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;
import static ru.epa.epabackend.util.StringPatterns.*;
import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс EmployeeRequestDto для передачи тела запроса на сервер с полными данными для создания и обновления сотрудника
 *
 * @author Валентина Вахламова
 */
@Builder
@Data
@AllArgsConstructor
public class RequestEmployeeDto {

    @NotBlank(groups = {Create.class})
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH)
    @Size(min = 1, max = 255, groups = {Create.class, Update.class})
    private String fullName;

    @Size(min = 5, max = 32, groups = {Create.class, Update.class})
    @Pattern(regexp = TELEGRAM)
    private String nickName;

    private String city;

    @NotBlank(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    @Size(min = 3, max = 255, groups = {Create.class, Update.class})
    private String email;

    @NotEmpty(groups = {Create.class})
    @Pattern(regexp = LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER, groups = {Create.class, Update.class})
    @Size(min = 8, max = 14, groups = {Create.class, Update.class})
    private String password;

    @Past(groups = {Create.class, Update.class})
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthday;

    @NotBlank(groups = {Create.class})
    @Size(min = 1, max = 255, groups = {Create.class, Update.class})
    @Pattern(regexp = CYRILLIC_LATIN_WHITESPACE_AND_DASH, groups = {Create.class, Update.class})
    private String position;

    @Size(min = 2, max = 255, groups = {Create.class, Update.class})
    private String department;

    @Override
    public String toString() {
        return "Employee{" +
                ", fullName='" + fullName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", password={masked}" +
                ", birthday=" + birthday +
                ", position=" + position +
                ", department=" + department +
                '}';
    }
}
