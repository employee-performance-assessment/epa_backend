package ru.epa.epabackend.task.employee;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.TaskOutDto;
import ru.epa.epabackend.exception.NotFoundException;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.task.employee.impl.TaskEmployeeServiceImpl;
import ru.epa.epabackend.util.Role;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.epa.epabackend.exception.ExceptionDescriptions.FORBIDDEN_TO_EDIT_NOT_YOUR_TASK;

@ExtendWith(MockitoExtension.class)
class TaskEmployeeUnitTests {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskEmployeeServiceImpl taskService;

    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final TaskStatus STATUS = TaskStatus.IN_PROGRESS;
    private Employee employee = new Employee();
    private Task task = new Task();
    private TaskOutDto taskOutDto = new TaskOutDto();

    @BeforeEach
    public void init() {
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.USER)
                .build();
        task = Task.builder()
                .id(ID_1)
                .basicPoints(10)
                .penaltyPoints(2)
                .finishDate(LocalDate.now().plusDays(2))
                .executor(employee)
                .status(STATUS)
                .build();
        taskOutDto = TaskOutDto.builder()
                .id(ID_1)
                .executor(employee)
                .build();
    }

    @Test
    void findAllTasksByEmployeeId_shouldCallRepository() {
        when(taskRepository.findAllByExecutorId(ID_1)).thenReturn(List.of(task));
        when(taskMapper.tasksToListOutDto(List.of(task))).thenReturn(List.of(taskOutDto));

        List<TaskOutDto> tasksResult = taskService.findAllByEmployeeId(ID_1);

        int expectedSize = 1;
        assertNotNull(tasksResult);
        assertEquals(expectedSize, tasksResult.size());
        verify(taskRepository, times(1)).findAllByExecutorId(ID_1);
    }

    @Test
    void findTaskById_shouldCallRepository() {
        when(taskRepository.findByIdAndExecutorId(task.getId(), employee.getId()))
                .thenReturn(Optional.ofNullable(task));
        when(taskMapper.taskUpdateToOutDto(task)).thenReturn(taskOutDto);

        TaskOutDto taskOutDtoResult = taskService.findById(employee.getId(), task.getId());

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
        when(taskMapper.taskUpdateToOutDto(task)).thenReturn(taskOutDto);

        TaskOutDto taskOutDtoResult = taskService.update(employee.getId(), task.getId(), STATUS);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void finById_shouldThrowNotFoundException_task() throws ValidationException {
        when(taskRepository.findByIdAndExecutorId(task.getId(), employee.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () ->
                taskService.findById(ID_2, ID_1));

        assertEquals(FORBIDDEN_TO_EDIT_NOT_YOUR_TASK.getTitle(), exception.getMessage());
    }
}