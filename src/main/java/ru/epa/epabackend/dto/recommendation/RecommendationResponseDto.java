package ru.epa.epabackend.dto.recommendation;

import lombok.*;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;

import java.time.LocalDate;

/**
 * Класс RecommendationResponseDto для передачи информации о рекомендации сотрудника.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResponseDto {

    /**
     * Идентификатор рекомендации.
     */
    private Long id;

    /**
     * Сотрудник, получивший рекомендацию.
     */
    private EmployeeShortResponseDto recipient;

    /**
     * Руководитель, отправивший рекомендацию.
     */
    private EmployeeShortResponseDto sender;

    /**
     * Дата создания рекомендации.
     */
    private LocalDate createDay;

    /**
     * Описание рекомендации.
     */
    private String recommendation;
}
