package ru.epa.epabackend.task.admin;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.employee.EmployeeShortDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.dto.task.TaskFullDto;
import ru.epa.epabackend.dto.task.TaskInDto;
import ru.epa.epabackend.dto.task.TaskShortDto;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.mapper.TaskMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.impl.EmployeeServiceImpl;
import ru.epa.epabackend.service.impl.ProjectServiceImpl;
import ru.epa.epabackend.service.impl.TaskServiceImpl;
import ru.epa.epabackend.util.Role;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAdminUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final TaskStatus STATUS = TaskStatus.IN_PROGRESS;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectServiceImpl projectService;
    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private ProjectMapper projectMapper;
    @InjectMocks
    private TaskServiceImpl taskService;
    private static final String email = "qwerty@gmail.com";
    private Employee admin = new Employee();
    private Employee employee = new Employee();
    private Task task = new Task();
    private TaskFullDto taskOutDto = new TaskFullDto();
    private TaskInDto taskInDto = new TaskInDto();
    private Project project = new Project();
    private ProjectShortDto projectShortDto = new ProjectShortDto();
    private TaskShortDto taskShortDto = new TaskShortDto();
    private EmployeeShortDto employeeShortDto;

    @BeforeEach
    public void init() {
        admin = Employee.builder()
                .id(ID_1)
                .role(Role.ROLE_ADMIN)
                .build();
        employeeShortDto = EmployeeShortDto.builder()
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
                .executor(employeeShortDto)
                .build();
        taskInDto = TaskInDto.builder()
                .executorId(ID_2)
                .projectId(ID_1)
                .deadLine(LocalDate.now().plusDays(2))
                .build();
        project = Project.builder()
                .id(ID_1)
                .name("Project1")
                .employees(List.of(employee))
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
    void findAllTasks_shouldCallRepository() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when((taskMapper.mapToShortDto(task))).thenReturn(taskShortDto);
        List<TaskShortDto> tasksResult = taskService.findAll();

        int expectedSize = 1;
        assertNotNull(tasksResult);
        assertEquals(expectedSize, tasksResult.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void findTaskById_shouldCallRepository() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        when(taskMapper.mapToFullDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.findDtoById(task.getId());

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).findById(admin.getId());
    }

    @Test
    void createTask_shouldCallRepository() {
        when(projectService.findById(project.getId())).thenReturn(project);
        when(employeeService.getEmployee(employee.getId())).thenReturn(employee);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.mapToEntity(taskInDto, project, employee)).thenReturn(task);
        when(taskMapper.mapToFullDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.create(taskInDto);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_shouldCallRepository() {
        when(projectService.findById(project.getId())).thenReturn(project);
        when(employeeService.getEmployee(employee.getId())).thenReturn(employee);
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.mapToFullDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.update(ID_1, taskInDto);

        int expectedId = 1;
        assertNotNull(taskOutDtoResult);
        assertEquals(expectedId, taskOutDtoResult.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_shouldCallRepository_status_done() {
        taskInDto.setStatus("DONE");
        when(projectService.findById(project.getId())).thenReturn(project);
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));
        when(employeeService.getEmployee(employee.getId())).thenReturn(employee);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.mapToFullDto(task)).thenReturn(taskOutDto);

        TaskFullDto taskOutDtoResult = taskService.update(ID_1, taskInDto);

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
        assertThrows(EntityNotFoundException.class, () -> taskService.findDtoById(ID_1));
    }
}
