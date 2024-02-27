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

@Builder
@Data
@AllArgsConstructor
public class EmployeeRtoRequest {

    @NotBlank
    @Size(min = 8, max = 152, groups = {Create.class, Update.class})
    private String fullName;

    @Size(min = 2, max = 50, groups = {Create.class, Update.class})
    private String nickName;

    private String city;

    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    @Size(max = 512, groups = {Create.class, Update.class})
    private String login;

    @NotEmpty(groups = {Create.class})
    @Size(min = 8, max = 14, groups = {Create.class, Update.class})
    private String password;

    @Past(groups = {Create.class, Update.class})
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthday;

    private Role role;

    @Size(min = 2, max = 255, groups = {Create.class, Update.class})
    private String position;

    @Size(min = 2, max = 255, groups = {Create.class, Update.class})
    private String department;

    @Override
    public String toString() {
        return "Employee{" +
                ", fullName='" + fullName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", city='" + city + '\'' +
                ", login='" + login + '\'' +
                ", password={masked}" +
                ", birthday=" + birthday +
                ", role=" + role +
                ", position=" + position +
                ", department=" + department +
                '}';
    }
}
