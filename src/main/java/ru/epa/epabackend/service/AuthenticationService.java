package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.employee.JwtRequest;
import ru.epa.epabackend.dto.employee.JwtResponse;

public interface AuthenticationService {
    JwtResponse getToken(JwtRequest jwtRequest);
}
