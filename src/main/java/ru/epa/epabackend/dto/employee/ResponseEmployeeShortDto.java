package ru.epa.epabackend.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.Role;

/**
 * Класс EmployeeShortResponseDto для передачи сокращенной информации о сотруднике, предназначенной для списков
 *
 * @author Валентина Вахламова
 */
@Builder
@Data
@AllArgsConstructor
public class ResponseEmployeeShortDto {
    private Long id;
    private String fullName;
    private String position;
    private String email;
    private Role role;
}
