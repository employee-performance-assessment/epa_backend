package ru.epa.epabackend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.dto.criteria.ResponseCriteriaDto;
import ru.epa.epabackend.dto.employee.ResponseEmployeeFullDto;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.time.LocalDate;
import java.util.List;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;

/**
 * Класс QuestionnaireFullResponseDto для передачи полной информации об анкете
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseQuestionnaireFullDto {
    private long id;

    private ResponseEmployeeFullDto author;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate created;

    private List<ResponseCriteriaDto> criterias;

    private QuestionnaireStatus status;
}
