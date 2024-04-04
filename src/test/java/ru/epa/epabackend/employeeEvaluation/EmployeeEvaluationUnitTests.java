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
import ru.epa.epabackend.dto.evaluation.RequestEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.service.impl.CriteriaServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeEvaluationServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeServiceImpl;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeEvaluationUnitTests {
    private static final long ID_0 = 0L;
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private final LocalDate startDay = LocalDate.now().minusDays(2);
    private final LocalDate endDay = LocalDate.now().plusDays(2);
    private static final String email = "qwerty@gmail.com";

    private static final String adminEmail = "adminEmail@gmail.com";
    @Mock
    private EmployeeEvaluationRepository employeeEvaluationRepository;
    @Mock
    private EmployeeServiceImpl employeeService;
    @Mock
    private CriteriaServiceImpl criteriaService;

    @Mock
    private QuestionnaireService questionnaireService;

    @Mock
    private EmployeeEvaluationMapper employeeEvaluationMapper;
    @InjectMocks
    private EmployeeEvaluationServiceImpl employeeEvaluationService;
    private Employee evaluator;
    private Employee evaluated;
    private Employee admin;
    private EmployeeEvaluation employeeEvaluation;
    private RequestEmployeeEvaluationDto requestEmployeeEvaluationDto;
    private ResponseRatingDto responseRatingDto;
    private Criteria criteria = new Criteria();
    private Questionnaire questionnaire = new Questionnaire();
    List<EmployeeEvaluation> evaluationList;
    List<RequestEmployeeEvaluationDto> evaluationRequestDtoList;

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
        admin = Employee.builder()
                .id(ID_0)
                .role(Role.ROLE_ADMIN)
                .email(adminEmail)
                .build();
        criteria = Criteria.builder()
                .id(ID_1)
                .name("Критерий")
                .build();
        questionnaire = Questionnaire.builder()
                .id(ID_1)
                .author(admin)
                .created(startDay.plusDays(1))
                .build();
        employeeEvaluation = EmployeeEvaluation.builder()
                .id(ID_1)
                .evaluator(evaluator)
                .evaluated(evaluated)
                .criteria(criteria)
                .build();
        requestEmployeeEvaluationDto = RequestEmployeeEvaluationDto.builder()
                .criteriaId(criteria.getId())
                .score(5)
                .build();
        evaluationList = new ArrayList<>();
        evaluationList.add(employeeEvaluation);
        evaluationRequestDtoList = new ArrayList<>();
        evaluationRequestDtoList.add(requestEmployeeEvaluationDto);
        responseRatingDto = ResponseRatingDto.builder()
                .rating(10.0)
                .build();
    }

    @Test
    @DisplayName("Сохранение оценки с вызовом репозитория")
    void shouldCreateEmployeeEvaluationWhenCallRepository() {
        when(employeeService.findById(ID_2)).thenReturn(evaluated);
        when(employeeService.findByEmail(email)).thenReturn(evaluator);
        when(criteriaService.findById(ID_1)).thenReturn(criteria);
        when(questionnaireService.findById(ID_1)).thenReturn(questionnaire);
        when(employeeEvaluationMapper.mapToEntity(requestEmployeeEvaluationDto,evaluated, questionnaire,evaluator,criteria))
                .thenReturn(employeeEvaluation);
        when(employeeEvaluationRepository.saveAll(evaluationList)).thenReturn(evaluationList);
        List<EmployeeEvaluation> employeeEvaluationListResult = employeeEvaluationService.
                create(email,evaluated.getId(), questionnaire.getId(), evaluationRequestDtoList);
        int expectedSize = 1;
        assertNotNull(employeeEvaluationListResult);
        assertEquals(expectedSize, employeeEvaluationListResult.size());
        verify(employeeEvaluationRepository,times(1)).saveAll(employeeEvaluationListResult);
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

   /* @Test
    @DisplayName("Получение списка своих оценок от коллег по своему email с вызовом репозитория")
    void shouldFindAllEvaluationsUsersWhenCallRepository() {
        when(employeeEvaluationRepository.findAllEvaluationsUsers(email))
                .thenReturn(List.of(responseEmployeeEvaluationDto));
        List<ResponseEmployeeEvaluationDto> responseEmployeeEvaluationDtoListResult = employeeEvaluationService
                .findAllEvaluationsUsers(email);
        int expectedSize = 1;
        assertNotNull(responseEmployeeEvaluationDtoListResult);
        assertEquals(expectedSize, responseEmployeeEvaluationDtoListResult.size());
        verify(employeeEvaluationRepository,times(1)).findAllEvaluationsUsers(email);
    }

    @Test
    @DisplayName("Получение списка своих оценок от руководителя по своему email с вызовом репозитория")
    void shouldFindAllEvaluationsAdminWhenCallRepository() {
        when(employeeEvaluationRepository.findAllEvaluationsAdmin(email))
                .thenReturn(List.of(responseEmployeeEvaluationDto));
        List<ResponseEmployeeEvaluationDto> responseEmployeeEvaluationDtoListResult = employeeEvaluationService
                .findAllEvaluationsAdmin(email);
        int expectedSize = 1;
        assertNotNull(responseEmployeeEvaluationDtoListResult);
        assertEquals(expectedSize, responseEmployeeEvaluationDtoListResult.size());
        verify(employeeEvaluationRepository,times(1)).findAllEvaluationsAdmin(email);
    }

    @Test
    @DisplayName("Получение рейтинга сотрудника от всего коллектива с вызовом репозитория")
    void shouldFindFullRatingWhenCallRepository() {
        when(employeeEvaluationRepository.findFullRating(email,startDay,endDay)).thenReturn(responseRatingDto);
        ResponseRatingDto responseRatingDtoResult = employeeEvaluationService
                .findFullRating(email,startDay,endDay);
        double expectedId = 10.0;
        assertNotNull(responseRatingDtoResult);
        assertEquals(expectedId, responseRatingDtoResult.getRating());
        verify(employeeEvaluationRepository,times(1)).findFullRating(email,startDay,endDay);
    }

    @Test
    @DisplayName("Получение рейтинга сотрудника только от руководителя с вызовом репозитория")
    void shouldFindRatingByAdminWhenCallRepository() {
        when(employeeEvaluationRepository.findRatingByAdmin(email,startDay,endDay)).thenReturn(responseRatingDto);
        ResponseRatingDto responseRatingDtoResult = employeeEvaluationService
                .findRatingByAdmin(email,startDay,endDay);
        double expectedId = 10.0;
        assertNotNull(responseRatingDtoResult);
        assertEquals(expectedId, responseRatingDtoResult.getRating());
        verify(employeeEvaluationRepository,times(1)).findRatingByAdmin(email,startDay,endDay);
    }*/
}
