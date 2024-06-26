package ru.epa.epabackend.task.employee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.task.ResponseTaskFullDto;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.impl.TaskServiceImpl;
import ru.epa.epabackend.util.Role;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskEmployeeUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final String STATUS = "IN_PROGRESS";
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private Principal principal;
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private TaskServiceImpl taskService;
    private Employee employee = new Employee();
    private Employee admin;
    private Task task = new Task();
    private ResponseTaskFullDto taskOutDto = new ResponseTaskFullDto();
    private ResponseEmployeeShortDto employeeShortDto;

    @BeforeEach
    public void init() {
        admin = Employee.builder()
                .id(ID_3)
                .build();
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .creator(admin)
                .build();
        employeeShortDto = ResponseEmployeeShortDto.builder()
                .id(ID_1)
                .fullName("name")
                .position("USER")
                .build();
        task = Task.builder()
                .id(ID_1)
                .basicPoints(10)
                .penaltyPoints(2)
                .finishDate(LocalDate.now().plusDays(2))
                .executor(employee)
                .status(TaskStatus.IN_PROGRESS)
                .build();
        taskOutDto = ResponseTaskFullDto.builder()
                .id(ID_1)
                .executor(employeeShortDto)
                .build();
    }

    @Test
    void findAllTasksByEmployeeId_shouldCallRepository() {
        when(taskRepository.findAllByExecutorIdAndText(ID_2, null)).thenReturn(List.of(task));
        when(employeeService.findByEmail(principal.getName())).thenReturn(employee);

        List<Task> tasksResult = taskService.findAllByExecutorEmail(principal, null);

        int expectedSize = 1;
        assertNotNull(tasksResult);
        assertEquals(expectedSize, tasksResult.size());
        verify(taskRepository, times(1)).findAllByExecutorIdAndText(ID_2, null);
    }

    @Test
    void findTaskById_shouldCallRepository() {
        when(taskRepository.findByIdAndOwnerId(task.getId(), admin.getId()))
                .thenReturn(Optional.ofNullable(task));
        when(employeeService.findByEmail(principal.getName())).thenReturn(employee);

        Task task = taskService.findByIdAndOwnerId(principal, this.task.getId());

        int expectedId = 1;
        assertNotNull(task);
        assertEquals(expectedId, task.getId());
        verify(taskRepository, times(1)).findByIdAndOwnerId(this.task.getId(), admin.getId());
    }

    @Test
    void updateTask_shouldCallRepository() {
        when(taskRepository.findByIdAndOwnerId(task.getId(), admin.getId()))
                .thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(employeeService.findByEmail(principal.getName())).thenReturn(employee);

        Task task = taskService
                .updateStatus(this.task.getId(), STATUS, principal);

        int expectedId = 1;
        assertNotNull(task);
        assertEquals(expectedId, task.getId());
        verify(taskRepository, times(1)).save(this.task);
    }

    @Test
    void finById_shouldThrowNotFoundException_task() throws ValidationException {
        when(taskRepository.findByIdAndOwnerId(task.getId(), admin.getId())).thenReturn(Optional.empty());
        when(employeeService.findByEmail(principal.getName())).thenReturn(employee);
        assertThrows(EntityNotFoundException.class, () -> taskService.findByIdAndOwnerId(principal, ID_1));
    }
}