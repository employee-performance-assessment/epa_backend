package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.ValidationGroups;

@Builder
@Data
@AllArgsConstructor
public class EmployeeShortRequestDto {
    @NotBlank
    @Size(min = 8, max = 512, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    protected String fullName;

    @NotBlank(groups = {ValidationGroups.Create.class})
    @Email(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(max = 512, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    protected String email;

    @NotEmpty(groups = {ValidationGroups.Create.class})
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
