package ru.epa.epabackend.model;

import lombok.*;

import java.util.List;

/**
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamAnalytics {
    private Integer completedOnTimePercent;
    private Integer delayedPercent;
    private List<Employee> leaders;
    private List<Employee> deadlineViolators;
}