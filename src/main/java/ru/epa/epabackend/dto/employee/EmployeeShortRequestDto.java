package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.ValidationGroups;

/**
 * Класс EmployeeShortRequestDto для передачи тела запроса на сервер с сокращенными данными для саморегистрации админа
 *
 * @author Валентина Вахламова
 */
@Builder
@Data
@AllArgsConstructor
public class EmployeeShortRequestDto {
    @NotBlank
    @Size(min = 1, max = 255, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^[а-яА-ЯЁё\\s\\-]+$")
    protected String fullName;

    @NotBlank(groups = {ValidationGroups.Create.class})
    @Email(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 5, max = 50, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    protected String email;

    @NotEmpty(groups = {ValidationGroups.Create.class})
    @Pattern(regexp = "^[a-zA-Z0-9\\.\\,\\:\\;\\?\\!\\*\\+\\%\\-\\<\\>\\@\\[\\]\\{\\}\\/\\\\\\_\\$\\#]+$",
            groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(min = 8, max = 14, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
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
