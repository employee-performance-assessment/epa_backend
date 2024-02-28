package ru.epa.epabackend.task.admin;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.TaskInDto;
import ru.epa.epabackend.dto.TaskOutDto;
import ru.epa.epabackend.exception.NotFoundException;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.task.admin.impl.TaskAdminServiceImpl;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.epa.epabackend.exception.ExceptionDescriptions.*;

@ExtendWith(MockitoExtension.class)
class TaskAdminUnitTests {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskAdminServiceImpl taskService;
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private Employee admin = new Employee();
    private Employee employee = new Employee();
    private Task task = new Task();
    private TaskOutDto taskOutDto = new TaskOutDto();
    private TaskInDto taskInDto = new TaskInDto();
    private Project project = new Project();

    @BeforeEach
    public void init() {
        admin = Employee.builder()
                .id(ID_1)
                .role(Role.ADMIN)
                .build();
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.USER)
                .build();
        task = Task.builder()
                .id(ID_1)
                .creator(admin)
                .basicPoints(10)
                .penaltyPoints(2)
                .finishDate(LocalDate.now().plusDays(2))
                .executor(employee)
                .build();
        taskOutDto = TaskOutDto.builder()
                .id(ID_1)
                .creator(admin)
                .executor(employee)
                .build();
        taskInDto = TaskInDto.builder()
                .creatorId(ID_1)
                .executorId(ID_2)
                .projectId(ID_1)
                .finishDate(LocalDate.now().plusDays(2))
                .build();
        project = Project.builder()
                .id(ID_1)
                .name("Project1")
                .build();
    }

    @Test
    void findAllTasks_shouldCallRepository() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.tasksToListOutDto(List.of(task))).thenReturn(List.of(taskOutDto));

        List<TaskOutDto> tasksResult = taskService.findAll();

        int expectedSize = 1;
        assertNotNull(tasksResult);
        assertEquals(expectedSize, tasksResult.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void findTaskById_shouldCallRepository() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskMapper.taskUpdateToOutDto(task)).thenReturn(taskOutDto);

        TaskOutDto taskOutDtoResult = taskService.findById(task.getId());

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).findById(admin.getId());
    }

    @Test
    void createTask_shouldCallRepository() {
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        when(employeeRepository.findById(admin.getId())).thenReturn(Optional.ofNullable(admin));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.ofNullable(employee));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.dtoInToTask(taskInDto)).thenReturn(task);
        when(taskMapper.taskCreateToOutDto(task)).thenReturn(taskOutDto);

        TaskOutDto taskOutDtoResult = taskService.create(taskInDto);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_shouldCallRepository() {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.ofNullable(employee));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskUpdateToOutDto(task)).thenReturn(taskOutDto);

        TaskOutDto taskOutDtoResult = taskService.update(ID_1, taskInDto);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_shouldCallRepository_status_done() {
        taskInDto.setStatus("DONE");
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.ofNullable(employee));
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskUpdateToOutDto(task)).thenReturn(taskOutDto);

        TaskOutDto taskOutDtoResult = taskService.update(ID_1, taskInDto);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteTask_shouldCallRepository() {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));

        taskService.delete(ID_1);

        verify(taskRepository, times(1)).delete(task);
    }


    @Test
    void finById_shouldThrowNotFoundException_task() throws ValidationException {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () ->
                taskService.findById(ID_1));

        assertEquals(TASK_NOT_FOUND.getTitle(), exception.getMessage());
    }

    @Test
    void create_shouldThrowNotFoundException_project() throws ValidationException {
        when(projectRepository.findById(ID_1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () ->
                taskService.create(taskInDto));

        assertEquals(PROJECT_NOT_FOUND.getTitle(), exception.getMessage());
    }

    @Test
    void create_shouldThrowNotFoundException_employee() throws ValidationException {
        when(employeeRepository.findById(ID_1)).thenReturn(Optional.empty());
        when(taskMapper.dtoInToTask(taskInDto)).thenReturn(task);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        Exception exception = assertThrows(NotFoundException.class, () ->
                taskService.create(taskInDto));

        assertEquals(EMPLOYEE_NOT_FOUND.getTitle(), exception.getMessage());
    }
}