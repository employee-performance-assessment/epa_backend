package ru.epa.epabackend.dto.criteria;

import lombok.*;

/**
 * Класс для передачи информации об критерии оценки
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseCriteriaDto {

    /**
     * Идентификатор критерия оценки
     */
    private Long id;

    /**
     * Название критерия оценки
     */
    private String name;

    /**
     * Является ли дефолтным критерием
     */
    private Boolean isDefault;
}
