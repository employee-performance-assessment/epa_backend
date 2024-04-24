package ru.epa.epabackend.dto.analytics;

import lombok.*;
import ru.epa.epabackend.dto.employee.ResponseEmployeeAnalyticsDto;

import java.util.List;

/**
 * Класс для передачи сокращенной информации о задаче, предназначенной для списков
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTeamAnalyticsFullDto {
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
    private List<ResponseEmployeeAnalyticsDto> leaders;
    private List<ResponseEmployeeAnalyticsDto> deadlineViolators;
}