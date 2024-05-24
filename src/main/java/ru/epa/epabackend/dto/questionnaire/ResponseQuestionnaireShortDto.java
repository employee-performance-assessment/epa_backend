package ru.epa.epabackend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.time.LocalDate;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;

/**
 * Класс для передачи сокращенной информации об анкете
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseQuestionnaireShortDto {
    private long id;

    private ResponseEmployeeShortDto author;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate created;

    private QuestionnaireStatus status;
}
