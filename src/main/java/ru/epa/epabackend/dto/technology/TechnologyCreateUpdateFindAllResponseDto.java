package ru.epa.epabackend.dto.technology;

import lombok.*;

/**
 * Класс TechnologyDto содержит структуру данных, которая используется для передачи
 * информации между различными слоями приложения.
 *
 * @author Артем Масалкин
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyCreateUpdateFindAllResponseDto {

    /**
     * Название технологии.
     */
    private String name;
}
