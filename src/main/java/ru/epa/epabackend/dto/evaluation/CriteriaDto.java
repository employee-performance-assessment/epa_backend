package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс EvaluationDto для передачи информации об критерии оценки.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaDto {

    /**
     * Идентификатор критерия оценки.
     */
    private Long id;

    /**
     * Название критерия оценки.
     */
    private String name;
}
