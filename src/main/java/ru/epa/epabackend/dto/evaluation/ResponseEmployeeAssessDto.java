package ru.epa.epabackend.dto.evaluation;

import lombok.*;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class ResponseEmployeeAssessDto {
    long employeeId;
    String employeeFullName;
    String employeePosition;
    long questionnaireId;
    LocalDate questionnaireCreated;
}
