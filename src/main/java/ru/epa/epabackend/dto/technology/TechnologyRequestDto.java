package ru.epa.epabackend.dto.technology;

import lombok.*;

/**
 * Класс TechnologyRequestDto для передачи тела запроса на сервер с данными для создания и обновления технологии
 *
 * @author Артем Масалкин
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyRequestDto {

    /**
     * Название технологии.
     */
    private String name;
}
