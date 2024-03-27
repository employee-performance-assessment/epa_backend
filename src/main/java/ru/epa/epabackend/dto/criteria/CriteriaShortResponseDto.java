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
public class CriteriaShortResponseDto {

    /**
     * Название критерия оценки.
     */
    private String name;
}
