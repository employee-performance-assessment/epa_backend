package ru.epa.epabackend.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;
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
public class EmployeeRequestDto {

    @NotBlank
    @Pattern(regexp = "^[а-яА-ЯЁё\\s\\-]+$")
    @Size(min = 1, max = 255, groups = {Create.class, Update.class})
    private String fullName;

    @Size(min = 5, max = 32, groups = {Create.class, Update.class})
    @Pattern(regexp = "^@{1}[a-zA-Z0-9\\_]+$")
    private String nickName;

    private String city;

    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    @Size(min = 5, max = 50, groups = {Create.class, Update.class})
    private String email;

    @NotEmpty(groups = {Create.class})
    @Pattern(regexp = "^[a-zA-Z0-9\\.\\,\\:\\;\\?\\!\\*\\+\\%\\-\\<\\>\\@\\[\\]\\{\\}\\/\\\\\\_\\$\\#]+$",
            groups = {Create.class, Update.class})
    @Size(min = 8, max = 14, groups = {Create.class, Update.class})
    private String password;

    @Past(groups = {Create.class, Update.class})
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthday;

    private Role role;

    @Size(min = 1, max = 255, groups = {Create.class, Update.class})
    @Pattern(regexp = "^[а-яА-ЯЁёa-zA-Z\\s\\-]+$")
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
                ", role=" + role +
                ", position=" + position +
                ", department=" + department +
                '}';
    }
}
