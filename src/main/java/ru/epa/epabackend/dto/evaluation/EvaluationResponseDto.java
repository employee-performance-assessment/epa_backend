package ru.epa.epabackend.dto.evaluation;

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
public class EvaluationResponseDto {

    /**
     * Название критерия оценки.
     */
    private String name;
}
