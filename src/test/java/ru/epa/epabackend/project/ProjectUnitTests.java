package ru.epa.epabackend.project;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.project.RequestProjectCreateDto;
import ru.epa.epabackend.dto.project.RequestProjectUpdateDto;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.impl.EmployeeServiceImpl;
import ru.epa.epabackend.service.impl.ProjectServiceImpl;
import ru.epa.epabackend.util.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectUnitTests {
    private final long ID_1 = 1L;
    private final long ID_2 = 2L;
    private final String email = "qwerty@gmail.com";
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private ProjectServiceImpl projectService;
    private Employee admin;
    private Employee employee;
    private Project project;
    private RequestProjectCreateDto requestProjectCreateDto;
    private RequestProjectUpdateDto requestProjectUpdateDto;
    private List<Employee> employees;
    private List<Employee> employeesWithEmployee;
    private Project projectWithEmployee;
    private List<Project> projects;

    @BeforeEach
    public void unit() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .projects(projects)
                .build();
        employees = new ArrayList<>();
        employees.add(admin);
        employeesWithEmployee = new ArrayList<>();
        employeesWithEmployee.add(admin);
        employeesWithEmployee.add(employee);
        project = Project.builder()
                .id(ID_1)
                .name("project")
                .employees(employees)
                .build();
        projectWithEmployee = Project.builder()
                .id(ID_1)
                .name("project")
                .employees(employeesWithEmployee)
                .build();
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .projects(List.of())
                .email(email)
                .projects(projects)
                .build();
        requestProjectCreateDto = RequestProjectCreateDto.builder()
                .name("projectCreate")
                .build();
        requestProjectUpdateDto = RequestProjectUpdateDto.builder()
                .name("projectUpdate")
                .build();
        projects = new ArrayList<>();
        projects.add(project);
        admin.setProjects(projects);
        employee.setProjects(projects);
    }

    @Test
    @DisplayName("Создание проекта с вызовом репозитория")
    void shouldCreateProjectWhenCallRepository() {
        when(projectRepository.save(project)).thenReturn(project);
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(projectMapper.mapToEntity(requestProjectCreateDto, List.of(admin))).thenReturn(project);
        Project project = projectService.create(requestProjectCreateDto, email);
        int expectedId = 1;
        assertNotNull(project);
        assertEquals(expectedId, project.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Получение проекта по id с исключением Not Found Exception")
    void shouldFindByIdProjectWhenThrowNotFoundException() throws ValidationException {
        when(projectRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> projectService.findById(ID_1));
    }

    @Test
    @DisplayName("Получение проекта по id с вызовом репозитория")
    void shouldFindByIdProjectWhenCallRepository() {
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        Project projectResult = projectService.findById(this.project.getId());
        long expectedId = 1L;
        assertEquals(expectedId, projectResult.getId());
        verify(projectRepository, times(1)).findById(projectResult.getId());
    }

    @Test
    @DisplayName("Получение проектов по email сотрудника или админа")
    void shouldFindAllByCreatorWhenCallRepositoryAndEmployee() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .projects(List.of(project))
                .build();
        when(employeeService.findByEmail(email)).thenReturn(employee);
        when(projectService.findAllByCreator(email)).thenReturn(List.of(project));
        List<Project> projectsResults = projectService.findAllByCreator(email);
        int expectedSize = 1;
        assertNotNull(projectsResults);
        assertEquals(expectedSize, projectsResults.size());
        verify(projectRepository, times(1)).findByEmployees(employee.getCreator());
    }

    @Test
    @DisplayName("Получение приекто по email админа")
    void shouldFindAllByCreatorWhenCallRepositoryAndAdmin() {
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(projectService.findAllByCreator(email)).thenReturn(List.of(project));
        List<Project> projectsResults = projectService.findAllByCreator(email);
        int expectedSize = 1;
        assertNotNull(projectsResults);
        assertEquals(expectedSize, projectsResults.size());
        verify(projectRepository, times(1)).findByEmployees(admin);
    }

    @Test
    @DisplayName("Обновление проекта")
    void shouldUpdateWhenCallRepository() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .projects(List.of(project))
                .build();
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        when(projectRepository.save(project)).thenReturn(project);
        Project projectResult = projectService.update(project.getId(), requestProjectUpdateDto, email);
        int expectedId = 1;
        assertNotNull(project);
        assertEquals(expectedId, projectResult.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Удаление проекта")
    void shouldDeleteWhenCallRepository() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .projects(List.of(project))
                .build();
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(taskRepository.existsByProjectId(ID_1)).thenReturn(false);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        projectService.delete(ID_1, email);
        verify(projectRepository, times(1)).delete(project);
    }
}
