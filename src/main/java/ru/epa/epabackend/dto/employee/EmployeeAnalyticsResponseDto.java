package ru.epa.epabackend.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Класс EmployeeAnalyticsResponseDto для передачи сокращенной информации о сотруднике, предназначенной для аналитики
 *
 * @author Владислав Осипов
 */
@Builder
@Data
@AllArgsConstructor
public class EmployeeAnalyticsResponseDto {
    private Long id;
    private String fullName;
    private String position;
}

