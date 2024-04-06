package ru.epa.epabackend.dto.evaluation;

import lombok.*;

import java.time.LocalDate;

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
public class ResponseEvaluatedQuestionnaireDto {

    /**
     * Идентификатор анкеты.
     */
    private Long idQuestionnaire;

    /**
     * Дата создания/рассылки анкеты.
     */
    private LocalDate createQuestionnaire;

    /**
     * Средний бал за анкету.
     */
    private Double middleScore;
}
