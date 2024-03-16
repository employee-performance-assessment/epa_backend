package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    @Email(groups = {Create.class, Update.class})
    @Size(min = 5, max = 50, groups = {Create.class, Update.class})
    private String email;

    @NotEmpty(groups = {Create.class})
    @Pattern(regexp = "^[a-zA-Z0-9\\.\\,\\:\\;\\?\\!\\*\\+\\%\\-\\<\\>\\@\\[\\]\\{\\}\\/\\\\\\_\\$\\#]+$",
            groups = {Create.class, Update.class})
    @Size(min = 8, max = 14, groups = {Create.class, Update.class})
    private String password;

    @Override
    public String toString() {
        return "JwtRequest{" +
                "email='" + email + '\'' +
                ", password={masked}" +
                '}';
    }
}