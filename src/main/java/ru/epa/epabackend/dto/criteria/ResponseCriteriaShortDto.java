package ru.epa.epabackend.dto.criteria;

import lombok.*;

/**
 * Класс EvaluationRequestDto для передачи информации об критерии оценки.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseCriteriaShortDto {

    /**
     * Идентификатор критерия оценки.
     */
    private long id;

    /**
     * Название критерия оценки.
     */
    private String name;
}
