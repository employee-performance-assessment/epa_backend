package ru.epa.epabackend.dto.evaluation;

import lombok.*;

/**
 * Класс EvaluationRequestDto для передачи информации об оценке
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
     * Название оценки.
     */
    private String name;
}
