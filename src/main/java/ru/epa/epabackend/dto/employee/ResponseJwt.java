package ru.epa.epabackend.dto.employee;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ResponseJwt {
    private final String token;
}
