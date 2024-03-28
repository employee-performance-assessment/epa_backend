package ru.epa.epabackend.employeeEvaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationResponseDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.service.CriteriaService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.impl.CriteriaServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeEvaluationServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeServiceImpl;
import ru.epa.epabackend.util.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeEvaluationUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final String email = "qwerty@gmail.com";
    @Mock
    private EmployeeEvaluationRepository employeeEvaluationRepository;
    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private CriteriaServiceImpl criteriaService;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeEvaluationMapper employeeEvaluationMapper;
    @InjectMocks
    private EmployeeEvaluationServiceImpl employeeEvaluationService;
    private Employee evaluator = new Employee();
    private Employee evaluated = new Employee();
    private EmployeeEvaluation employeeEvaluation = new EmployeeEvaluation();
    private EmployeeEvaluationDto employeeEvaluationDto = new EmployeeEvaluationDto();
    private EmployeeEvaluationRequestDto employeeEvaluationRequestDto = new EmployeeEvaluationRequestDto();
    private EmployeeEvaluationResponseDto employeeEvaluationResponseDto = new EmployeeEvaluationResponseDto();
    private EmployeeShortResponseDto employeeShortDto;
    private Criteria criteria = new Criteria();

    @BeforeEach
    public void init() {
        employeeShortDto = EmployeeShortResponseDto.builder()
                .id(ID_1)
                .fullName("name")
                .position("USER")
                .build();
        evaluator = Employee.builder()
                .id(ID_1)
                .role(Role.ROLE_USER)
                .email(email)
                .build();
        evaluated = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .email(email)
                .build();
        criteria = Criteria.builder()
                .id(ID_1)
                .name("Критерий")
                .build();
        employeeEvaluation = EmployeeEvaluation.builder()
                .id(ID_1)
                .evaluator(evaluator)
                .evaluated(evaluated)
                .criteria(criteria)
                .build();
        employeeEvaluationDto = EmployeeEvaluationDto.builder()
                .id(ID_1)
                .evaluated(employeeShortDto)
                .score(5)
                .build();
        employeeEvaluationRequestDto = EmployeeEvaluationRequestDto.builder()
                .criteriaId(criteria.getId())
                .score(5)
                .build();
    }
/*
    @Test
    @DisplayName("Создание оценки с вызовом репозитория")
    void shouldCreateEmployeeEvaluationWhenCallRepository() {
        when(employeeService.findById(ID_2)).thenReturn(evaluated);
        when(employeeService.findById(ID_1)).thenReturn(evaluator);
        when(criteriaService.findById(employeeEvaluationRequestDto.getCriteriaId())).thenReturn(criteria);

        EmployeeEvaluation employeeEvaluation = employeeEvaluationService.create(evaluator.getId(), evaluated.getId(),
                employeeEvaluationRequestDto);

        long expectedId = 1;
        assertEquals(expectedId, employeeEvaluation.getId());
        verify(employeeEvaluationRepository, times(1)).save(this.employeeEvaluation);
    }

 */

}
