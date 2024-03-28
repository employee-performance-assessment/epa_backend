package ru.epa.epabackend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.dto.employee.EmployeeFullResponseDto;
import ru.epa.epabackend.dto.evaluation.CriteriaResponseDto;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.time.LocalDate;
import java.util.Set;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;

/**
 * Класс QuestionnaireFullResponseDto для передачи полной информации об анкете
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireFullResponseDto {
    private long id;

    private EmployeeFullResponseDto author;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate created;

    private Set<CriteriaResponseDto> criterias;

    private QuestionnaireStatus status;
}
