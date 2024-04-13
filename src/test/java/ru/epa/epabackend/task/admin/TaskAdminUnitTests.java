package ru.epa.epabackend.task.admin;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.task.RequestTaskDto;
import ru.epa.epabackend.dto.task.ResponseTaskFullDto;
import ru.epa.epabackend.dto.task.ResponseTaskShortDto;
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
    private static final String email = "qwerty@gmail.com";
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectServiceImpl projectService;
    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private ProjectMapper projectMapper;
    @InjectMocks
    private TaskServiceImpl taskService;
    private Employee admin = new Employee();
    private Employee employee = new Employee();
    private Task task = new Task();
    private ResponseTaskFullDto taskOutDto = new ResponseTaskFullDto();
    private RequestTaskDto taskInDto = new RequestTaskDto();
    private Project project = new Project();
    private ResponseTaskShortDto taskShortDto = new ResponseTaskShortDto();
    private ResponseEmployeeShortDto employeeShortDto;

    @BeforeEach
    public void init() {
        admin = Employee.builder()
                .id(ID_1)
                .role(Role.ROLE_ADMIN)
                .build();
        employeeShortDto = ResponseEmployeeShortDto.builder()
                .id(ID_1)
                .fullName("name")
                .position("USER")
                .build();
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .creator(admin)
                .email(email)
                .build();
        project = Project.builder()
                .id(ID_1)
                .name("Project1")
                .employees(List.of(employee))
                .build();
        task = Task.builder()
                .id(ID_1)
                .basicPoints(10)
                .penaltyPoints(2)
                .finishDate(LocalDate.now().plusDays(2))
                .executor(employee)
                .project(project)
                .build();
        taskOutDto = ResponseTaskFullDto.builder()
                .id(ID_1)
                .executor(employeeShortDto)
                .build();
        taskInDto = RequestTaskDto.builder()
                .executorId(ID_2)
                .projectId(ID_1)
                .deadLine(LocalDate.now().plusDays(2))
                .build();
        taskShortDto = ResponseTaskShortDto.builder()
                .id(ID_1)
                .name("taskShort")
                .deadLine(LocalDate.now().plusDays(2))
                .status(STATUS)
                .basicPoints(10)
                .build();
    }

    @Test
    void findAllTasks_shouldCallRepository() {
        when(taskRepository.findAllByOwnerEmail(email)).thenReturn(List.of(task));
        List<Task> tasksResult = taskService.findAll(email);

        int expectedSize = 1;
        assertNotNull(tasksResult);
        assertEquals(expectedSize, tasksResult.size());
        verify(taskRepository, times(1)).findAllByOwnerEmail(email);
    }

    @Test
    void findTaskById_shouldCallRepository() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));

        Task task = taskService.findDtoById(this.task.getId(), email);

        int expectedId = 1;
        assertNotNull(task);
        assertEquals(expectedId, task.getId());
        verify(taskRepository, times(1)).findById(admin.getId());
    }

    @Test
    void createTask_shouldCallRepository() {
        when(projectService.findById(project.getId())).thenReturn(project);
        when(employeeService.findById(employee.getId())).thenReturn(employee);
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.mapToEntity(taskInDto, project, employee, admin)).thenReturn(task);

        Task task = taskService.create(taskInDto, email);

        int expectedId = 1;
        assertNotNull(task);
        assertEquals(expectedId, task.getId());
        verify(taskRepository, times(1)).save(this.task);
    }

    @Test
    void updateTask_shouldCallRepository() {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));
        when(employeeService.findById(employee.getId())).thenReturn(employee);
        when(taskRepository.save(task)).thenReturn(task);

        Task task = taskService.update(ID_1, taskInDto, email);

        int expectedId = 1;
        assertNotNull(task);
        assertEquals(expectedId, task.getId());
        verify(taskRepository, times(1)).save(this.task);
    }

    @Test
    void updateTask_shouldCallRepository_status_done() {
        taskInDto.setStatus("DONE");
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));
        when(employeeService.findById(employee.getId())).thenReturn(employee);
        when(taskRepository.save(task)).thenReturn(task);

        Task task = taskService.update(ID_1, taskInDto, email);

        int expectedId = 1;
        assertNotNull(task);
        assertEquals(expectedId, task.getId());
        verify(taskRepository, times(1)).save(this.task);
    }

    @Test
    void deleteTask_shouldCallRepository() {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.ofNullable(task));

        taskService.delete(ID_1, email);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void finById_shouldThrowNotFoundException_task() throws ValidationException {
        when(taskRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.findDtoById(ID_1, email));
    }
}