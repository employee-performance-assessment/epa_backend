package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ru.epa.epabackend.util.StringPatterns.LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestJwt {

    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    private String email;

    @NotEmpty
    @Pattern(regexp = LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER)
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