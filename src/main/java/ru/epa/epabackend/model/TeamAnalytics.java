package ru.epa.epabackend.model;

import lombok.*;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;

import java.util.List;

/**
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAnalytics {
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
    private List<EmployeeShortResponseDto> leaders;
    private List<EmployeeShortResponseDto> deadlineViolators;
}