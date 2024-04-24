package ru.epa.epabackend.dto.technology;

import lombok.*;

/**
 * Класс для передачи информации о технологии
 *
 * @author Артем Масалкин
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTechnologyDto {

    /**
     * Id технологии
     */
    private Long id;

    /**
     * Название технологии
     */
    private String name;
}
