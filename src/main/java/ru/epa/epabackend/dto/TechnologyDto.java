package ru.epa.epabackend.dto;

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
public class TechnologyDto {

    /**
     * Идентификатор технологии.
     */
    private Long id;

    /**
     * Название технологии.
     */
    private String name;
}
