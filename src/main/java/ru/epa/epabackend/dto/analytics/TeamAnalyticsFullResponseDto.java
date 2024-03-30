package ru.epa.epabackend.dto.analytics;

import lombok.*;
import ru.epa.epabackend.dto.employee.EmployeeAnalyticsResponseDto;

import java.util.List;

/**
 * Класс TaskAnalyticsFullResponseDto для передачи сокращенной информации о задаче, предназначенной для списков
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAnalyticsFullResponseDto {
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
    private List<EmployeeAnalyticsResponseDto> leaders;
    private List<EmployeeAnalyticsResponseDto> deadlineViolators;
}