package ru.epa.epabackend.dto.employee;

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
public class EmployeeShortAnalyticsResponseDto {
    private Long id;
    private String fullName;
    private String position;
    private Double completedOnTimePercent;
    private Double notCompletedOnTimePercent;
}