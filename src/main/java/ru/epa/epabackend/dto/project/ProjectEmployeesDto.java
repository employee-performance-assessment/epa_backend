package ru.epa.epabackend.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.epa.epabackend.model.Employee;

import java.util.List;

/**
 * Класс ProjectEmployeesDto для передачи информации о проекте включая список сотрудников
 *
 * @author Константин Осипов
 */
@Data
@AllArgsConstructor
public class ProjectEmployeesDto {
    private Long id;
    private String name;
    private List<Employee> employees;
}
