package ru.epa.epabackend.dto.task;

import lombok.*;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;

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
public class TaskAnalyticsFullResponseDto {
    private Double teamCompletedOnTimePercent;
    private Double teamNotCompletedOnTimePercent;
    private List<EmployeeShortResponseDto> leaders;
    private List<EmployeeShortResponseDto> deadlineViolators;
}