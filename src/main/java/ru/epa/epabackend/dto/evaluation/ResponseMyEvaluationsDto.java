package ru.epa.epabackend.dto.evaluation;

import lombok.*;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireShortDto;

import java.util.List;
import java.util.Objects;

/**
 * Класс ResponseEmployeeEvaluationsByQuestionnaireIdDto для передачи информации
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
public class ResponseMyEvaluationsDto {

    /**
     * Сокращенная информация об анкете.
     */
    private ResponseQuestionnaireShortDto questionnaire;

    /**
     * Список оценок коллеги.
     */
    private List<ResponseEmployeeEvaluationShortDto> responseEmployeeEvaluationList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseMyEvaluationsDto that = (ResponseMyEvaluationsDto) o;
        return Objects.equals(questionnaire, that.questionnaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionnaire);
    }
}
