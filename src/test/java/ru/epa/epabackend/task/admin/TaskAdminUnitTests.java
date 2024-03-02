package ru.epa.epabackend.task.admin;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseShort;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.exception.exceptions.NotFoundException;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeServiceImpl;
import ru.epa.epabackend.service.project.ProjectServiceImpl;
import ru.epa.epabackend.service.task.TaskServiceImpl;
import ru.epa.epabackend.util.Role;
import ru.epa.epabackend.util.TaskStatus;

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
    private ProjectMapper projectMapper;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectServiceImpl projectService;
    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private TaskServiceImpl taskService;
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final String email = "qwerty@gmail.com";
    private static final TaskStatus STATUS = TaskStatus.IN_PROGRESS;
    private Employee admin = new Employee();
    private Employee employee = new Employee();
    private Task task = new Task();
    private TaskFullDto taskOutDto = new TaskFullDto();
    private TaskInDto taskInDto = new TaskInDto();
    private Project project = new Project();
    private ProjectShortDto projectShortDto = projectMapper.toProjectShortDto(project);
    private TaskShortDto taskShortDto = new TaskShortDto();
    private EmployeeDtoResponseShort employeeDtoResponseShort;

    @BeforeEach
    public void init() {
        admin = Employee.builder()
                .id(ID_1)
                .role(Role.ROLE_ADMIN)
                .build();
        employeeDtoResponseShort = EmployeeDtoResponseShort.builder()
                .id(ID_1)
                .fullName("name")
                .position("USER")
                .build();
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .email(email)
                .build();
        task = Task.builder()
                .id(ID_1)
                .basicPoints(10)
                .penaltyPoints(2)
                .finishDate(LocalDate.now().plusDays(2))
                .executor(employee)
                .build();
        taskOutDto = TaskFullDto.builder()
                .id(ID_1)
                .executor(employeeDtoResponseShort)
                .build();
        taskInDto = TaskInDto.builder()
                .executorId(ID_2)
                .projectId(ID_1)
                .deadLine(LocalDate.now().plusDays(2))
                .build();
        project = new Project()
                .setId(ID_1)
                .setName("Project1");
        taskShortDto = TaskShortDto.builder()
                .id(ID_1)
                .name("taskShort")
                .deadLine(LocalDate.now().plusDays(2))
                .status(STATUS)
                .basicPoints(10)
                .build();
    }

    @Test
    void findAllTasks_shouldCallRepository() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.tasksToListOutDto(List.of(task))).thenReturn(List.of(taskShortDto));

        List<TaskShortDto> tasksResult = taskService.findAllByAdmin();

        int expectedSize = 1;
        assertNotNull(tasksResult);
        assertEquals(expectedSize, tasksResult.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void findTaskById_shouldCallRepository() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskMapper.taskUpdateToOutDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.findByIdByAdmin(task.getId());

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).findById(admin.getId());
    }

    @Test
    void createTask_shouldCallRepository() {
        when(projectService.findDtoById(project.getId(), email)).thenReturn(projectShortDto);
        when(employeeService.getEmployee(employee.getId())).thenReturn(employee);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.dtoInToTask(taskInDto)).thenReturn(task);
        when(taskMapper.taskCreateToOutDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.createByAdmin(taskInDto, email);

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

        TaskFullDto taskOutDtoResult = taskService.updateByAdmin(ID_1, taskInDto);

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

        TaskFullDto taskOutDtoResult = taskService.updateByAdmin(ID_1, taskInDto);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void deleteTask_shouldCallRepository() {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));

        taskService.deleteByAdmin(ID_1);

        verify(taskRepository, times(1)).delete(task);
    }


    @Test
    void finById_shouldThrowNotFoundException_task() throws ValidationException {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () ->
                taskService.findByIdByAdmin(ID_1));

        assertEquals(TASK_NOT_FOUND.getTitle(), exception.getMessage());
    }
}