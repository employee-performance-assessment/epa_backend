package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс ResponseEvaluationsAdminUserDto для передачи информации
 * о поставленных оценках сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseEvaluationsAdminUserDto {

    /**
     * Оценка руководителя.
     */
    private Double adminScore;

    /**
     * Оценка сотрудников.
     */
    private Double colleaguesScore;
}
