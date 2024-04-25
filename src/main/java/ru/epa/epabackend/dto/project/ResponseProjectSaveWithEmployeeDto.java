package ru.epa.epabackend.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;

import java.util.List;

/**
 * Класс для передачи информации о проекте включая список сотрудников
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProjectSaveWithEmployeeDto {
    private Long id;
    private String name;
    private List<ResponseEmployeeShortDto> employees;
}
