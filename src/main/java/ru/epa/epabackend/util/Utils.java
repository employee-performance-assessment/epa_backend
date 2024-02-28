package ru.epa.epabackend.util;

import ru.epa.epabackend.dto.TaskInDto;
import ru.epa.epabackend.exception.NotFoundException;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;

import static ru.epa.epabackend.exception.ExceptionDescriptions.EMPLOYEE_NOT_FOUND;
import static ru.epa.epabackend.exception.ExceptionDescriptions.PROJECT_NOT_FOUND;

public class Utils {

    public static void setNotNullParamToEntity(TaskInDto taskInDto, Task task, EmployeeRepository employeeRepository,
                                               ProjectRepository projectRepository) {
        if (taskInDto.getName() != null) {
            task.setName(taskInDto.getName());
        }

        if (taskInDto.getDescription() != null) {
            task.setName(taskInDto.getDescription());
        }

        if (taskInDto.getExecutorId() != null) {
            Employee employee = employeeRepository.findById(taskInDto.getExecutorId())
                    .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND.getTitle()));
            task.setExecutor(employee);
        }

        if (taskInDto.getFinishDate() != null) {
            task.setFinishDate(taskInDto.getFinishDate());
        }

        if (taskInDto.getBasicPoints() != null) {
            task.setBasicPoints(taskInDto.getBasicPoints());
        }

        if (taskInDto.getProjectId() != null) {
            Project project = projectRepository.findById(taskInDto.getProjectId())
                    .orElseThrow(() -> new NotFoundException(PROJECT_NOT_FOUND.getTitle()));
            task.setProject(project);
        }

        if (taskInDto.getPenaltyPoints() != null) {
            task.setPenaltyPoints(taskInDto.getPenaltyPoints());
        }

        if (taskInDto.getStatus() != null) {
            task.setStatus(TaskStatus.from(taskInDto.getStatus())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + taskInDto.getStatus())));
        }
    }
}