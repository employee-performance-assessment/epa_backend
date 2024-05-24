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

    @NotBlank(message = "email не должен быть пустым")
    @Email(message = "Поле email имеет некорректный формат")
    @Size(min = 3, max = 255, message = "Допустимая длина поля email от 3 до 255 символов")
    private String email;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Pattern(regexp = LATIN_NUMBERS_SPECIAL_CHARACTERS_AND_ONE_UPPERCASE_LETTER,
            message = "В поле пароль разрешены английские символы, цифры и спецсимволы ,:;?!*+%-<>@[]/\\_{}$#")
    @Size(min = 8, max = 14, message = "Допустимая длина пароля от 8 до 14 символов")
    private String password;

    @Override
    public String toString() {
        return "JwtRequest{" +
                "email='" + email + '\'' +
                ", password={masked}" +
                '}';
    }
}