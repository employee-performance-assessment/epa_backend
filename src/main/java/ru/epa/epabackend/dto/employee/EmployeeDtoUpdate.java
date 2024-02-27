package ru.epa.epabackend.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;

@Builder
@Data
@AllArgsConstructor
public class EmployeeDtoUpdate {

    private Long id;

    @Size(min = 8, max = 152)
    private String fullName;

    @Size(min = 2, max = 50)
    private String nickName;

    private String city;

    @Email
    @Size(max = 512)
    private String login;

    @Size(min = 8, max = 14)
    private String password;

    @Past
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthday;

    private Role role;

    @Size(min = 2, max = 255)
    private String position;

    @Size(min = 2, max = 255)
    private String department;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
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
