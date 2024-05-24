package ru.epa.epabackend.dto.analytics;

import lombok.*;

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
public class ResponseTeamAnalyticsShortDto {
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
}