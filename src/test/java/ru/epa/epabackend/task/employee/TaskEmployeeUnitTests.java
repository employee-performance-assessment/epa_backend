package ru.epa.epabackend.task.employee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.impl.TaskServiceImpl;
import ru.epa.epabackend.util.Role;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskEmployeeUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final TaskStatus STATUS = TaskStatus.IN_PROGRESS;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskServiceImpl taskService;
    private Employee employee = new Employee();
    private Task task = new Task();
    private TaskFullDto taskOutDto = new TaskFullDto();
    private TaskShortDto taskShortDto = new TaskShortDto();
    private EmployeeShortDto employeeShortDto;

    @BeforeEach
    public void init() {
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .build();
        employeeShortDto = EmployeeShortDto.builder()
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
                .status(STATUS)
                .build();
        taskOutDto = TaskFullDto.builder()
                .id(ID_1)
                .executor(employeeShortDto)
                .build();
        taskShortDto = TaskShortDto.builder()
                .id(ID_1)
                .name("taskShort")
                .deadLine(LocalDate.now().plusDays(2))
                .status(STATUS)
                .basicPoints(10)
                .build();
    }

    @Test
    void findAllTasksByEmployeeId_shouldCallRepository() {
        when(taskRepository.findAllByExecutorId(ID_1)).thenReturn(asList(task));
        List<TaskShortDto> tasks = new ArrayList<>();
        tasks.add(taskMapper.mapToShortDto(task));
        List<TaskShortDto> taskShortDtoList = new ArrayList<>();
        taskShortDtoList.add(taskShortDto);

        List<TaskShortDto> tasksResult = taskService.findAllByEmployeeId(ID_1);

        int expectedSize = 1;
        assertNotNull(tasksResult);
        assertEquals(expectedSize, tasksResult.size());
        verify(taskRepository, times(1)).findAllByExecutorId(ID_1);
    }

    @Test
    void findTaskById_shouldCallRepository() {
        when(taskRepository.findByIdAndExecutorId(task.getId(), employee.getId()))
                .thenReturn(Optional.ofNullable(task));
        when(taskMapper.mapToFullDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.findById(employee.getId(), task.getId());

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).findByIdAndExecutorId(task.getId(), employee.getId());
    }

    @Test
    void updateTask_shouldCallRepository() {
        when(taskRepository.findByIdAndExecutorId(task.getId(), employee.getId()))
                .thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.mapToFullDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.updateStatus(employee.getId(), task.getId(), STATUS);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void finById_shouldThrowNotFoundException_task() throws ValidationException {
        when(taskRepository.findByIdAndExecutorId(task.getId(), employee.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.findById(ID_2, ID_1));
    }
}
