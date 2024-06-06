package ru.epa.epabackend.dto.employee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestRefreshJwt {
    @NotBlank(message = "Токен обновления не должен быть пустым")
    private String refreshToken;

    @Override
    public String toString() {
        return "RequestRefreshJwt{" +
                "refreshToken={masked}}";
    }
}
