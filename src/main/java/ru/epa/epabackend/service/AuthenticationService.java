package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.employee.RequestJwt;
import ru.epa.epabackend.dto.employee.ResponseJwt;

public interface AuthenticationService {
    ResponseJwt getToken(RequestJwt requestJwt);
}
