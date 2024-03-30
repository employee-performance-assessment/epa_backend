package ru.epa.epabackend.dto.analytics;

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
public class TeamAnalyticsShortResponseDto {
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
}