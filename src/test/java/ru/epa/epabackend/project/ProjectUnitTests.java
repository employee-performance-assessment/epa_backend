package ru.epa.epabackend.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectCreateRequestDto;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.impl.ProjectServiceImpl;
import ru.epa.epabackend.util.ProjectStatus;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private ProjectServiceImpl projectService;
    private Employee admin = new Employee();
    private Employee employee = new Employee();
    private Project project = new Project();
    private EmployeeShortResponseDto employeeShortDto;
    private ProjectCreateRequestDto projectCreateRequestDto = new ProjectCreateRequestDto();

    @BeforeEach
    public void unit(){
        admin = Employee.builder()
                .id(ID_1)
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
                .status(ProjectStatus.DISTRIBUTED)
                .created(LocalDate.now())
                .build();
        projectCreateRequestDto = ProjectCreateRequestDto.builder()
                .name("projectCreate")
                .build();

    }

    @Test
    @DisplayName("Создание проекта с вызовом репозитория")
    void shouldCreateProjectWhenCallRepository() {
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.mapToEntity(projectCreateRequestDto, List.of(admin))).thenReturn(project);
        Project project = projectService.create(projectCreateRequestDto, email);
        int expectedId = 1;
        assertNotNull(project);
        assertEquals(expectedId, project.getId());
        verify(projectRepository, times(1)).save(this.project);

    }
}
