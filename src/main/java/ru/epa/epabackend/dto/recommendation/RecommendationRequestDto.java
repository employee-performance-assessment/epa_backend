package ru.epa.epabackend.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Класс RecommendationRequestDto для передачи рекомендации на сервер.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationRequestDto {

    /**
     * Описание рекомендации.
     */
    @NotBlank
    @Size(min = 10, max = 600)
    private String description;
}
