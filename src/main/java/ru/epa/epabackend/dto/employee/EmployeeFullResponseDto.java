package ru.epa.epabackend.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;

@Builder
@Data
@AllArgsConstructor
public class EmployeeFullResponseDto {

    private Long id;

    private String fullName;

    private String nickName;

    private String city;

    private String email;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthday;

    private Role role;

    private String position;

    private String department;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
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
