package ru.epa.epabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class EmployeeDtoShort {

    private String fullName;

    private String position;
}
