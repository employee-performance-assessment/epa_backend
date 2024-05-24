package ru.epa.epabackend.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Класс для передачи сокращенной информации о сотруднике, предназначенной для аналитики
 *
 * @author Владислав Осипов
 */
@Builder
@Data
@AllArgsConstructor
public class ResponseEmployeeAnalyticsDto {
    private Long id;
    private String fullName;
    private String position;
}

