package ru.epa.epabackend.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс EmployeeShortAnalyticsResponseDto для передачи сокращенной информации о сотруднике и аналитике сделанных им
 * задач.
 *
 * @author Владислав Осипов
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualAnalyticsResponseDto {
    private Long employeeId;
    private String employeeFullName;
    private String employeePosition;
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
}