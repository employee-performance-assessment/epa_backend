package ru.epa.epabackend.employeeEvaluation;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.service.impl.CriteriaServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeEvaluationServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeServiceImpl;
import ru.epa.epabackend.util.Role;

import java.util.List;
import java.util.Optional;

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
    private EmployeeEvaluationMapper employeeEvaluationMapper;
    @InjectMocks
    private EmployeeEvaluationServiceImpl employeeEvaluationService;
    private Employee evaluator;
    private Employee evaluated;
    private EmployeeEvaluation employeeEvaluation;
    private EmployeeEvaluationRequestDto employeeEvaluationRequestDto;
    private Criteria criteria = new Criteria();

    @BeforeEach
    public void init() {
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
        employeeEvaluationRequestDto = EmployeeEvaluationRequestDto.builder()
                .criteriaId(criteria.getId())
                .score(5)
                .build();
    }

    @Test
    @DisplayName("Создание оценки с вызовом репозитория")
    void shouldCreateEmployeeEvaluationWhenCallRepository() {
        when(employeeService.findById(ID_2)).thenReturn(evaluated);
        when(employeeService.findById(ID_1)).thenReturn(evaluator);
        when(criteriaService.findById(ID_1)).thenReturn(criteria);
        when(employeeEvaluationMapper.mapToEntity(employeeEvaluationRequestDto,evaluated,evaluator,criteria))
                .thenReturn(employeeEvaluation);
        when(employeeEvaluationRepository.save(employeeEvaluation)).thenReturn(employeeEvaluation);
        EmployeeEvaluation employeeEvaluationResult = employeeEvaluationService
                .create(evaluator.getId(),evaluated.getId(),employeeEvaluationRequestDto);
        int expectedId = 1;
        assertNotNull(employeeEvaluationResult);
        assertEquals(expectedId, employeeEvaluationResult.getId());
        verify(employeeEvaluationRepository,times(1)).save(employeeEvaluationResult);
    }

    @Test
    @DisplayName("Поиск оценки по Id с исключением Not Found Exception")
    void shouldFindByIdWhenThrowNotFoundException() throws ValidationException {
        when(employeeEvaluationRepository.findById(ID_1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> employeeEvaluationService.findById(ID_1));
    }

    @Test
    @DisplayName("Поиск оценки по Id с вызовом репозитория")
    void shouldFindByIdProjectWhenCallRepository() {
        when(employeeEvaluationRepository.findById(employeeEvaluation.getId()))
                .thenReturn(Optional.ofNullable(employeeEvaluation));
        EmployeeEvaluation employeeEvaluationResult = employeeEvaluationService
                .findById(this.employeeEvaluation.getId());
        long expectedId = 1L;
        assertEquals(expectedId, employeeEvaluationResult.getId());
        verify(employeeEvaluationRepository, times(1))
                .findById(employeeEvaluationResult.getId());
    }

    @Test
    @DisplayName("Удаление оценки по Id оценщика с вызовом репозитория")
    void shouldFindAllByAppraiserId(){
        when(employeeEvaluationRepository.findAllByEvaluatedId(ID_2)).thenReturn(List.of(employeeEvaluation));
        List<EmployeeEvaluation> employeeEvaluationsResult = employeeEvaluationService
                .findAllByAppraiserId(evaluated.getId());
        int expectedSize = 1;
        assertNotNull(employeeEvaluationsResult);
        assertEquals(expectedSize, employeeEvaluationsResult.size());
        verify(employeeEvaluationRepository,times(1)).findAllByEvaluatedId(evaluated.getId());
    }

    @Test
    @DisplayName("Удаление оценки с вызовом репозитория")
    void shouldDeleteTechnologyWhen() {
        when(employeeEvaluationRepository.existsById(any())).thenReturn(true);
        employeeEvaluationService.delete(ID_1);
        verify(employeeEvaluationRepository, times(1)).existsById(ID_1);
    }


}
