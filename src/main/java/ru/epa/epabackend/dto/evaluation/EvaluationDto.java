package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс EvaluationDto для передачи информации об оценке
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationDto {

    /**
     * Идентификатор оценки.
     */
    private Long id;

    /**
     * Название оценки.
     */
    private String name;
}
