package ru.epa.epabackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.Create;
import ru.epa.epabackend.util.Role;
import ru.epa.epabackend.util.Update;

import java.time.LocalDate;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;

@Builder
@Data
@AllArgsConstructor
public class EmployeeDto {

    Long id;

    @NotBlank(groups = {Create.class})
    @Size(min = 8, max = 152, groups = {Create.class, Update.class})
    private String fullName;

    //TODO: pattern
    @Size(min = 2, max = 50, groups = {Create.class, Update.class})
    private String nik;

    //TODO: pattern
    private String city;

    //TODO: pattern
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    @Size(max = 512, groups = {Create.class, Update.class})
    private String login;

    //TODO: pattern
    @NotBlank(groups = {Create.class})
    @Size(min = 8, max = 14, groups = {Create.class, Update.class})
    private String password;

    @Past
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate birthday;

    private Role role;

    @Size(min = 2, max = 255, groups = {Create.class, Update.class})
    private String position;

    @Size(min = 2, max = 255, groups = {Create.class, Update.class})
    private String department;
}
