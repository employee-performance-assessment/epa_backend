package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.epa.epabackend.util.ValidationGroups;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    @Email(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Size(max = 512, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String login;

    @NotEmpty(groups = {ValidationGroups.Create.class})
    @Size(min = 8, max = 14, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String password;

    @Override
    public String toString() {
        return "JwtRequest{" +
                "login='" + login + '\'' +
                ", password={masked}" +
                '}';
    }
}