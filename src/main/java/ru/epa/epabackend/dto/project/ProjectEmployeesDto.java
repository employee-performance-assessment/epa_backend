package ru.epa.epabackend.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.model.Employee;

import java.util.List;

/**
 * Класс ProjectEmployeesDto для передачи информации о проекте включая список сотрудников
 *
 * @author Константин Осипов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectEmployeesDto {
    private Long id;
    private String name;
    private List<Employee> employees;
}
