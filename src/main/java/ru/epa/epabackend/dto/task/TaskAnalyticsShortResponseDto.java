package ru.epa.epabackend.dto.task;

import lombok.*;

/**
 * Класс TaskAnalyticsShortResponseDto для передачи сокращенной информации о задаче, предназначенной для списков
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAnalyticsShortResponseDto {
    private Double teamCompletedOnTimePercent;
    private Double teamNotCompletedOnTimePercent;
}