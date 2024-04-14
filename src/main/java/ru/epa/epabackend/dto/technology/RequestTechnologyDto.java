package ru.epa.epabackend.dto.technology;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

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
public class RequestTechnologyDto {

    /**
     * Название технологии.
     */
    @NotBlank(message = "Название не должно быть пустым")
    @Size(min = 3, max = 255, groups = {Create.class, Update.class}, message = "Допустимая длина названия технологии " +
            "от 3 до 255 символов")
    private String name;
}
