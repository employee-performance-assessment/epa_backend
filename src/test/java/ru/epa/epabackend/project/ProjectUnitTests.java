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
import org.yaml.snakeyaml.events.Event;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.dto.project.ProjectUpdateRequestDto;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
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
    @InjectMocks
    private ProjectServiceImpl projectService;
    private Employee admin;
    private Employee employee;
    private Employee employee1;
    private Project project;
    private ProjectCreateRequestDto projectCreateRequestDto;
    private ProjectUpdateRequestDto projectUpdateRequestDto;
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
        projectCreateRequestDto = ProjectCreateRequestDto.builder()
                .name("projectCreate")
                .build();
        projectUpdateRequestDto = ProjectUpdateRequestDto.builder()
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
        when(projectMapper.mapToEntity(projectCreateRequestDto, List.of(admin))).thenReturn(project);
        Project project = projectService.create(projectCreateRequestDto, email);
        int expectedId = 1;
        assertNotNull(project);
        assertEquals(expectedId, project.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Поиск проекта по Id с исключением Not Found Exception")
    void shouldFindByIdProjectWhenThrowNotFoundException() throws ValidationException {
        when(projectRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> projectService.findById(ID_1));
    }

    @Test
    @DisplayName("Поиск проекта по Id с вызовом репозитория")
    void shouldFindByIdProjectWhenCallRepository() {
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        Project projectResult = projectService.findById(this.project.getId());
        long expectedId = 1L;
        assertEquals(expectedId, projectResult.getId());
        verify(projectRepository, times(1)).findById(projectResult.getId());
    }
    @Test
    @DisplayName("Сохранение сотрудника в проект")
    void shouldSaveWithEmployeeWhenCallRepository() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .projects(List.of(project))
                .build();
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(employeeService.findById(ID_2)).thenReturn(employee);
        when(projectRepository.findById(ID_1)).thenReturn(Optional.of(project));
        when(projectRepository.save(projectWithEmployee)).thenReturn(projectWithEmployee);
        Project projectResult = projectService.saveWithEmployee(project.getId(),employee.getId(),email);
        int expectedId = 1;
        assertNotNull(projectWithEmployee);
        assertEquals(expectedId, projectResult.getId());
        verify(projectRepository,times(1)).save(project);
    }


    @Test
    @DisplayName("Поиск всех создателей пользователю")
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
    @DisplayName("Поиск всех создателей по админу")
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
    @DisplayName("Поиск всех по id и роли")
    void shouldFindAllByProjectIdAndRoleWhenCallRepository() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .projects(List.of(project))
                .build();
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        when(projectService.findAllByProjectIdAndRole(project.getId(), Role.ROLE_ADMIN, email)).thenReturn(List.of(employee));
        List<Employee> employeeResults = projectService.findAllByProjectIdAndRole(project.getId(), Role.ROLE_ADMIN, email);
        int expectedSize = 1;
        assertNotNull(employeeResults);
        assertEquals(expectedSize, employeeResults.size());
        verify(employeeRepository, times(1)).
                findByProjectsAndRole(projectService.findById(project.getId()), Role.ROLE_ADMIN);
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
        Project projectResult = projectService.update(project.getId(), projectUpdateRequestDto, email);
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
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        projectService.delete(ID_1, email);
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    @DisplayName("Удаление сотрудника из проекта")
    void shouldDeleteEmployeeFromProjectWhenCallRepository() {
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(projectRepository.findById(projectWithEmployee.getId())).thenReturn(Optional.of(projectWithEmployee));
        when(employeeService.findById(ID_2)).thenReturn(employee);
        when(projectRepository.save(project)).thenReturn(project);
        projectService.deleteEmployeeFromProject(ID_1, ID_2, employee.getEmail());
        verify(projectRepository, times(1)).save(project);
    }
}
