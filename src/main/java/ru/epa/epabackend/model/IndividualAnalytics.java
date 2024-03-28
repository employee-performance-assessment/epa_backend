package ru.epa.epabackend.model;

import lombok.*;

/**
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndividualAnalytics {
    private Long employeeId;
    private String employeeFullName;
    private String employeePosition;
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
}