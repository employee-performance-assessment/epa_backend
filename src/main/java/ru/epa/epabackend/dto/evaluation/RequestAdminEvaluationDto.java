package ru.epa.epabackend.dto.evaluation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * Класс EvaluationCreateRequestDto для передачи тела запроса на сервер для создания оценки.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAdminEvaluationDto {

    /**
     * Список оценок руководителя.
     */
    private List<RequestEmployeeEvaluationDto> evaluationDtoList;

    /**
     * Описание рекомендации.
     */
    @NotBlank
    @Size(min = 10, max = 600)
    private String recommendation;
}