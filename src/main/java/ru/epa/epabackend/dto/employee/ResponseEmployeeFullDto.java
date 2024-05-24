package ru.epa.epabackend.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;

import static ru.epa.epabackend.util.DateConstant.DATE_PATTERN;

/**
 * Класс для передачи полной информации о сотруднике
 *
 * @author Валентина Вахламова
 */
@Builder
@Data
@AllArgsConstructor
public class ResponseEmployeeFullDto {

    private Long id;

    private String fullName;

    private String email;

    private Role role;

    private String position;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDate created;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password={masked}" +
                ", role=" + role +
                ", position=" + position +
                ", created=" + created +
                '}';
    }
}
