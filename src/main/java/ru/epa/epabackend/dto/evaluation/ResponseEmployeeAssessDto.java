package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class ResponseEmployeeAssessDto {
    long employeeId;
    String employeeFullName;
    String employeePosition;
    Role evaluatorRole;
    long questionnaireId;
    LocalDate questionnaireCreated;
}
