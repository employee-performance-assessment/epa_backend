package ru.epa.epabackend.dto.recommendation;

import lombok.*;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireShortDto;

import java.time.LocalDate;

/**
 * Класс RecommendationResponseDto для передачи информации о рекомендации сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseRecommendationDto {

    /**
     * Идентификатор рекомендации.
     */
    private Long id;

    /**
     * Сотрудник, получивший рекомендацию.
     */
    private ResponseEmployeeShortDto recipient;

    /**
     * Руководитель, отправивший рекомендацию.
     */
    private ResponseEmployeeShortDto sender;

    /**
     * Дата создания рекомендации.
     */
    private LocalDate createDay;

    /**
     * Описание рекомендации.
     */
    private String recommendation;

    /**
     * Анкета по которой оцениваем.
     */
    private ResponseQuestionnaireShortDto questionnaire;
}
