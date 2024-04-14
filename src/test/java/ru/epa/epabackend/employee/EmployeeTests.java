package ru.epa.epabackend.employee;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.epa.epabackend.dto.employee.RequestEmployeeDto;
import ru.epa.epabackend.dto.employee.RequestEmployeeShortDto;
import ru.epa.epabackend.exception.exceptions.ConflictException;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.repository.EmployeeRepository;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.impl.EmployeeServiceImpl;
import ru.epa.epabackend.util.Role;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final String email = "qwerty@gmail.com";


    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeEvaluationRepository employeeEvaluationRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    private Employee admin;
    private Employee employee;
    private RequestEmployeeDto requestEmployeeDto;
    private RequestEmployeeShortDto requestEmployeeShortDto;


    @BeforeEach
    public void unit() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email)
                .role(Role.ROLE_ADMIN)
                .build();
        employee = Employee.builder()
                .id(ID_2)
                .email(email)
                .creator(admin)
                .build();
        requestEmployeeDto = RequestEmployeeDto.builder()
                .email(email)
                .password("12345")
                .build();
        requestEmployeeShortDto = RequestEmployeeShortDto.builder()
                .email(email)
                .password("12345")
                .build();
    }

    @Test
    @DisplayName("Создание нового сотрудника с вызовом репозитория")
    void shouldCreateWhenCallRepository() {
        when(employeeMapper.mapToEntity(requestEmployeeDto)).thenReturn(employee);
        employee.setPassword("12345");
        employee.setRole(Role.ROLE_USER);
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(admin));
        employee.setCreator(admin);
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee employeeResult = employeeService.create(requestEmployeeDto,email);
        int expectedId = 2;
        assertNotNull(employeeResult);
        assertEquals(expectedId, employeeResult.getId());
        verify(employeeRepository,times(1)).save(employeeResult);
    }

    @Test
    @DisplayName("Создание нового сотрудника с вызовом репозитория")
    void shouldCreateSelfRegisterWhenCallRepository() {
        when(employeeMapper.mapToEntity(requestEmployeeShortDto)).thenReturn(employee);
        employee.setPassword("12345");
        employee.setRole(Role.ROLE_ADMIN);
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee employeeResult = employeeService.createSelfRegister(requestEmployeeShortDto);
        int expectedId = 2;
        assertNotNull(employeeResult);
        assertEquals(expectedId, employeeResult.getId());
        verify(employeeRepository,times(1)).save(employeeResult);
    }

    @Test
    @DisplayName("Обновление сотрудника с вызовом репозитория")
    void shouldUpdateWhenCallRepository() {
        when(employeeRepository.findById(ID_2)).thenReturn(Optional.of(employee));
        employee.setPassword("12345");
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(admin));
        employee.setCreator(admin);
        int expectedId = 2;

        Employee employeeResult = employeeService.update(employee.getId(), requestEmployeeDto, email);

        assertNotNull(employeeResult);
        assertEquals(expectedId, employeeResult.getId());
        verify(employeeRepository,times(1)).save(employeeResult);
    }

    @Test
    @DisplayName("Удаление сотрудника с вызовом репозитория")
    void shouldDeleteWhenCallRepository() {
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(admin));
        when(employeeRepository.findById(ID_2)).thenReturn(Optional.of(employee));
        when(employeeEvaluationRepository.existsByEvaluatedIdOrEvaluatorId(ID_2, ID_2)).thenReturn(false);
        when(taskRepository.existsByExecutorId(ID_2)).thenReturn(false);
        employeeService.delete(ID_2, email);
        verify(employeeRepository, times(1)).findByEmail(email);
        verify(employeeRepository, times(1)).findById(ID_2);
        verify(employeeEvaluationRepository, times(1)).existsByEvaluatedIdOrEvaluatorId(ID_2, ID_2);
        verify(taskRepository, times(1)).existsByExecutorId(ID_2);
        verify(employeeRepository, times(1)).deleteById(ID_2);
    }


    @Test
    @DisplayName("Получение всех сотрудников с вызовом репозитория")
    void shouldFindAllWhenCallRepository() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        List<Employee> employeeResult = employeeService.findAll();
        assertNotNull(employeeResult);
        assertEquals(1, employeeResult.size());
        verify(employeeRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Получение сотрудника по email с исключением Not Found Exception")
    void shouldFindBEmailEmployeeWhenThrowNotFoundException() throws ValidationException {
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> employeeService.findByEmail(email));
    }

    @Test
    @DisplayName("Получение сотрудника по email с вызовом репозитория")
    void shouldFindByEmailEmployeeWhenCallRepository() {
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.ofNullable(employee));
        Employee employeeResult = employeeService.findByEmail(this.employee.getEmail());
        String expectedEmail = "qwerty@gmail.com";
        assertEquals(expectedEmail, employeeResult.getEmail());
        verify(employeeRepository, times(1)).findByEmail(employeeResult.getEmail());
    }

    @Test
    @DisplayName("Получение сотрудника по id с исключением Not Found Exception")
    void shouldFindByIdEmployeeWhenThrowNotFoundException() throws ValidationException {
        when(employeeRepository.findById(ID_2)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> employeeService.findById(ID_2));
    }

    @Test
    @DisplayName("Получение сотрудника по id с вызовом репозитория")
    void shouldFindByIdEmployeeWhenCallRepository() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.ofNullable(employee));
        Employee employeeResult = employeeService.findById(this.employee.getId());
        long expectedId = 2L;
        assertEquals(expectedId, employeeResult.getId());
        verify(employeeRepository, times(1)).findById(employeeResult.getId());
    }

    @Test
    @DisplayName("Получение всех сотрудников для одного админа с вызовом репозитория")
    void shouldFindAllByCreatorEmailWhenCallRepository() {
        when(employeeRepository.findAllByCreatorEmail(email, Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(List.of(employee));
        List<Employee> employeeResult = employeeService.findAllByCreatorEmail(email);
        assertNotNull(employeeResult);
        assertEquals(1, employeeResult.size());
        verify(employeeRepository, times(1))
                .findAllByCreatorEmail(employee.getEmail(), Sort.by(Sort.Direction.ASC, "id"));
    }
}
