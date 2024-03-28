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
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final String email = "qwerty@gmail.com";
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
    private Project project;
    private EmployeeShortResponseDto employeeShortDto;
    private ProjectCreateRequestDto projectCreateRequestDto;
    private ProjectUpdateRequestDto projectUpdateRequestDto;

    @BeforeEach
    public void unit() {
        project = Project.builder()
                .id(ID_1)
                .name("project")
                .build();
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .build();
        employeeShortDto = EmployeeShortResponseDto.builder()
                .id(ID_1)
                .fullName("name")
                .position("USER")
                .build();
        employee = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .email(email)
                .build();
        project = Project.builder()
                .id(ID_1)
                .name("project")
                .build();
        projectCreateRequestDto = ProjectCreateRequestDto.builder()
                .name("projectCreate")
                .build();
        projectUpdateRequestDto = ProjectUpdateRequestDto.builder()
                .name("projectUpdate")
                .build();


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
    @DisplayName("Поиск технологии по Id с исключением Not Found Exception")
    void shouldFindByIdProjectWhenThrowNotFoundException() throws ValidationException {
        when(projectRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> projectService.findById(ID_1));
    }

    @Test
    @DisplayName("Поиск технологии по Id с вызовом репозитория")
    void shouldFindByIdProjectWhenCallRepository() {
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        Project projectRe = projectService.findById(this.project.getId());
        long expectedId = 1L;
        assertEquals(expectedId, projectRe.getId());
        verify(projectRepository, times(1)).findById(projectRe.getId());
    }

/*
        @Test
        @DisplayName("Сохранение с сотрудником")
        void shouldSaveWithEmployeeWhenCallRepository(){
            when(employeeService.findByEmail(email)).thenReturn(admin);
            when(employeeService.findById(ID_2)).thenReturn(employee);
            when(projectRepository.findById(ID_1)).thenReturn(Optional.ofNullable(project));
            long expectedId = 1L;
            Project projectRe = projectService.saveWithEmployee(project.getId(),employee.getId(),email);
            assertEquals(expectedId, projectRe.getId());
            verify(projectRepository, times(1)).save(project);
        }

    @Test
    @DisplayName("Поиск всех создателей")
    void shouldFindAllByCreatorWhenCallRepository() {
        //when(projectRepository.findByEmployees(employee)).thenReturn(List.of(project));
        when(employeeService.findByEmail(email)).thenReturn(employee);
        List<Project> projectResults = projectService.findAllByCreator(email);
        long expectedSize = 1L;
        assertNotNull(projectResults);
        assertEquals(expectedSize, projectResults.size());
        verify(projectRepository, times(1)).findByEmployees(employee);
    }



    @Test
    @DisplayName("Поиск всех по id и роли")
    void shouldFindAllByProjectIdAndRoleWhenCallRepository(){
        //when(employeeService.findByEmail(email)).thenReturn(admin);
        //when(projectRepository.findById(ID_1)).thenReturn(Optional.of(project));

    }

    @Test
    @DisplayName("Обновление проекта")
    void shouldUpdateWhenCallRepository(){
        when(employeeService.findByEmail(email)).thenReturn(admin);
        when(projectRepository.findById(ID_1)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        Project project = projectService.update(ID_1,projectUpdateRequestDto, email);
        long expectedId = 1L;
        assertNotNull(project);
        assertEquals(expectedId,project.getId());

        verify(projectRepository,times(1)).save(project);
    }

 */

}
