package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9\\.\\,\\:\\;\\?\\!\\*\\+\\%\\-\\<\\>\\@\\[\\]\\{\\}\\/\\\\\\_\\$\\#]+$")
    @Size(min = 8, max = 14)
    private String password;

    @Override
    public String toString() {
        return "JwtRequest{" +
                "email='" + email + '\'' +
                ", password={masked}" +
                '}';
    }
}