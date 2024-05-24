package ru.epa.epabackend.employeeEvaluation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.*;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.repository.RecommendationRepository;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.service.RecommendationService;
import ru.epa.epabackend.service.impl.CriteriaServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeEvaluationServiceImpl;
import ru.epa.epabackend.service.impl.EmployeeServiceImpl;
import ru.epa.epabackend.util.Role;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeEvaluationUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private final LocalDate startDay = LocalDate.now().minusDays(2);
    private final LocalDate endDay = LocalDate.now().plusDays(2);
    private static final String email1 = "qwerty1@gmail.com";
    private static final String email2 = "qwerty2@gmail.com";

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
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private RecommendationService recommendationService;
    @InjectMocks
    private EmployeeEvaluationServiceImpl employeeEvaluationService;
    private Employee evaluator;
    private Employee evaluated;
    private Employee admin;
    private EmployeeEvaluation employeeEvaluation;
    private RequestEmployeeEvaluationDto requestEmployeeEvaluationDto;
    private ResponseRatingDto responseRatingDto;
    private Criteria criteria;
    private Questionnaire questionnaire;
    private ResponseRatingFullDto responseRatingFullDto;
    private ResponseEmployeeEvaluationQuestionnaireDto responseEmployeeEvaluationQuestionnaireDto;
    private ResponseEmployeeEvaluationShortDto responseEmployeeEvaluationShortDtoAdmin;
    private ResponseEmployeeEvaluationShortDto responseEmployeeEvaluationShortDtoEmployee1;
    private ResponseEmployeeEvaluationShortDto responseEmployeeEvaluationShortDtoEmployee2;
    private Recommendation recommendation;
    private HashMap<String, ResponseEvaluationsAdminUserDto> evaluations;
    private ResponseEvaluationsAdminUserDto responseEvaluationsAdminUserDto;
    private ResponseEmployeeAssessDto responseEmployeeAssessDto;
    private ResponseAdminEvaluationDto responseAdminEvaluationDto;
    private ResponseEvaluatedQuestionnaireDto responseEvaluatedQuestionnaireDto;
    List<ResponseEmployeeEvaluationShortDto> adminEvaluations;
    List<ResponseEmployeeEvaluationShortDto> userEvaluations;
    List<EmployeeEvaluation> employeeEvaluationList;
    List<RequestEmployeeEvaluationDto> evaluationRequestDtoList;
    List<ResponseRatingFullDto> responseRatingFullDtoList;
    List<Employee> employees;
    List<ResponseEmployeeAssessDto> responseEmployeeAssessDtoList;
    List<ResponseEvaluatedQuestionnaireDto> responseEvaluatedQuestionnaireDtoList;

    @BeforeEach
    public void init() {
        admin = Employee.builder()
                .id(ID_1)
                .role(Role.ROLE_ADMIN)
                .email(adminEmail)
                .build();
        evaluator = Employee.builder()
                .id(ID_2)
                .role(Role.ROLE_USER)
                .creator(admin)
                .email(email1)
                .build();
        evaluated = Employee.builder()
                .id(ID_3)
                .role(Role.ROLE_USER)
                .creator(admin)
                .email(email2)
                .build();
        criteria = Criteria.builder()
                .id(ID_1)
                .name("Критерий")
                .build();
        questionnaire = Questionnaire.builder()
                .id(ID_1)
                .author(admin)
                .created(startDay.plusDays(1))
                .criterias(List.of(criteria))
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
        employeeEvaluationList = new ArrayList<>();
        employeeEvaluationList.add(employeeEvaluation);
        evaluationRequestDtoList = new ArrayList<>();
        evaluationRequestDtoList.add(requestEmployeeEvaluationDto);
        responseRatingDto = ResponseRatingDto.builder()
                .rating(10.0)
                .build();
        responseRatingFullDto = ResponseRatingFullDto.builder()
                .monthNumber(4)
                .rating(10.0)
                .build();
        responseRatingFullDtoList = new ArrayList<>();
        responseRatingFullDtoList.add(responseRatingFullDto);
        employees = new ArrayList<>();
        employees.add(evaluated);
        employees.add(admin);
        responseEmployeeEvaluationQuestionnaireDto = ResponseEmployeeEvaluationQuestionnaireDto.builder()
                .recommendation("recommendation")
                .createQuestionnaire(startDay)
                .middleScore(20.0)
                .build();
        adminEvaluations = new ArrayList<>();
        userEvaluations = new ArrayList<>();
        responseEmployeeEvaluationShortDtoAdmin = ResponseEmployeeEvaluationShortDto.builder()
                .name("admin")
                .score(10.0)
                .build();
        responseEmployeeEvaluationShortDtoEmployee1 = ResponseEmployeeEvaluationShortDto.builder()
                .name("employee1")
                .score(20.0)
                .build();
        responseEmployeeEvaluationShortDtoEmployee2 = ResponseEmployeeEvaluationShortDto.builder()
                .name("employee2")
                .score(30.0)
                .build();
        adminEvaluations.add(responseEmployeeEvaluationShortDtoEmployee1);
        adminEvaluations.add(responseEmployeeEvaluationShortDtoEmployee2);
        userEvaluations.add(responseEmployeeEvaluationShortDtoEmployee1);
        recommendation = Recommendation.builder()
                .id(ID_1)
                .recipient(admin)
                .recommendation("recommendation")
                .createDay(LocalDate.now())
                .build();
        responseEvaluationsAdminUserDto = ResponseEvaluationsAdminUserDto.builder()
                .adminScore(10.0)
                .colleaguesScore(20.0)
                .build();
        evaluations = new HashMap<>();
        evaluations.put("eva", responseEvaluationsAdminUserDto);
        responseEmployeeEvaluationQuestionnaireDto.setEvaluations(evaluations);
        responseEvaluatedQuestionnaireDto = ResponseEvaluatedQuestionnaireDto.builder()
                .idQuestionnaire(ID_1)
                .createQuestionnaire(startDay)
                .middleScore(10.0)
                .build();
        responseEvaluatedQuestionnaireDtoList = new ArrayList<>();
        responseEvaluatedQuestionnaireDtoList.add(responseEvaluatedQuestionnaireDto);
    }

    @Test
    @DisplayName("Сохранение оценки с вызовом репозитория")
    void shouldCreateEmployeeEvaluationWhenCallRepository() {
        when(employeeService.findById(ID_3)).thenReturn(evaluated);
        when(employeeService.findByEmail(email1)).thenReturn(evaluator);
        when(criteriaService.findById(ID_1)).thenReturn(criteria);
        when(questionnaireService.findById(ID_1)).thenReturn(questionnaire);
        when(employeeEvaluationMapper.mapToEntity(requestEmployeeEvaluationDto, evaluated, questionnaire, evaluator, criteria))
                .thenReturn(employeeEvaluation);
        when(employeeEvaluationRepository.saveAll(employeeEvaluationList)).thenReturn(employeeEvaluationList);
        List<EmployeeEvaluation> employeeEvaluationListResult = employeeEvaluationService.
                create(evaluator.getEmail(), evaluated.getId(), questionnaire.getId(), evaluationRequestDtoList);
        int expectedSize = 1;
        assertNotNull(employeeEvaluationListResult);
        assertEquals(expectedSize, employeeEvaluationListResult.size());
        verify(employeeEvaluationRepository, times(1)).saveAll(employeeEvaluationListResult);
    }

    @Test
    @DisplayName("Получение командного рейтинга за каждый месяц указанного года с вызовом репозитория")
    void shouldFindCommandRatingWhenCallRepository() {
        when(employeeService.findByEmail(email1)).thenReturn(evaluator);
        when(employeeEvaluationRepository.findCommandRating(ID_1, 2024)).thenReturn(responseRatingFullDtoList);
        List<ResponseRatingFullDto> responseRatingFullDtoListResult = employeeEvaluationService
                .findCommandRating(email1, 2024);
        int expectedSize = 1;
        double expectedRating = 10.0;
        int expectedMonthNumber = 4;
        assertNotNull(responseRatingFullDtoListResult);
        assertEquals(expectedSize, responseRatingFullDtoListResult.size());
        assertEquals(expectedRating, responseRatingFullDtoListResult.get(0).getRating());
        assertEquals(expectedMonthNumber, responseRatingFullDtoListResult.get(0).getMonthNumber());
    }

    @Test
    @DisplayName("Получение персонального рейтинга за каждый месяц для сотрудника с вызовом репозитория")
    void shouldFindPersonalRatingWhenCallRepository() {
        when(employeeEvaluationRepository.findPersonalRating(email1, 2024)).thenReturn(responseRatingFullDtoList);
        List<ResponseRatingFullDto> responseRatingFullDtoListResult = employeeEvaluationService
                .findPersonalRating(email1, 2024);
        int expectedSize = 1;
        double expectedRating = 10.0;
        int expectedMonthNumber = 4;
        assertNotNull(responseRatingFullDtoListResult);
        assertEquals(expectedSize, responseRatingFullDtoListResult.size());
        assertEquals(expectedRating, responseRatingFullDtoListResult.get(0).getRating());
        assertEquals(expectedMonthNumber, responseRatingFullDtoListResult.get(0).getMonthNumber());
    }

    @Test
    @DisplayName("Получение оценок по id анкеты")
    void shouldFindAllEvaluationsByQuestionnaireId() {
        when(employeeEvaluationRepository.findAllEvaluationsAdmin(adminEmail, ID_1)).thenReturn(adminEvaluations);
        when(employeeEvaluationRepository.findAllEvaluationsUsers(adminEmail, ID_1)).thenReturn(userEvaluations);
        when(questionnaireService.findById(ID_1)).thenReturn(questionnaire);
        when(employeeService.findByEmail(adminEmail)).thenReturn(admin);
        when(recommendationRepository.findByRecipientEmailAndQuestionnaireId(adminEmail, ID_1))
                .thenReturn(recommendation);
        when(employeeEvaluationRepository.findRatingByQuestionnaireIdAndEvaluatedEmail(ID_1, adminEmail))
                .thenReturn(responseRatingDto);
        ResponseEmployeeEvaluationQuestionnaireDto responseEmployeeEvaluationQuestionnaireDtoResult =
                employeeEvaluationService.findAllEvaluationsByQuestionnaireId(adminEmail, ID_1);
        String expectedRecommendation = "recommendation";
        LocalDate expectedDate = startDay.plusDays(1);
        double expectedScore = 10.0;
        assertNotNull(responseEmployeeEvaluationQuestionnaireDtoResult);
        assertEquals(expectedRecommendation, responseEmployeeEvaluationQuestionnaireDtoResult.getRecommendation());
        assertEquals(expectedDate,
                responseEmployeeEvaluationQuestionnaireDtoResult.getCreateQuestionnaire());
        assertEquals(expectedScore, responseEmployeeEvaluationQuestionnaireDtoResult.getMiddleScore());
    }

    @Test
    @DisplayName("Получение оценок по email оценивающего, id анкеты и id сотрудника")
    void shouldFindAllEvaluationsByQuestionnaireIdForAdmin() {
        when(questionnaireService.findById(ID_1)).thenReturn(questionnaire);
        when(employeeService.findByEmail(adminEmail)).thenReturn(admin);
        when(employeeService.findById(ID_3)).thenReturn(evaluated);
        when(employeeEvaluationRepository.findAllEvaluationsForAdmin(adminEmail, ID_3, ID_1))
                .thenReturn(adminEvaluations);
        when(employeeEvaluationRepository.findAllEvaluationsUsersForAdmin(ID_3, ID_1)).thenReturn(userEvaluations);
        when(recommendationRepository.getByRecipientIdAndQuestionnaireId(ID_3, ID_1)).thenReturn(recommendation);
        when(employeeEvaluationRepository.findRatingByQuestionnaireIdAndEvaluatedId(ID_1, ID_3))
                .thenReturn(responseRatingDto);
        ResponseEmployeeEvaluationQuestionnaireDto responseEmployeeEvaluationQuestionnaireDtoResult =
                employeeEvaluationService
                        .findAllEvaluationsByQuestionnaireIdForAdmin(adminEmail, questionnaire.getId(), evaluated.getId());
        String expectedRecommendation = "recommendation";
        LocalDate expectedDate = startDay.plusDays(1);
        double expectedScore = 10.0;
        assertNotNull(responseEmployeeEvaluationQuestionnaireDtoResult);
        assertEquals(expectedRecommendation, responseEmployeeEvaluationQuestionnaireDtoResult.getRecommendation());
        assertEquals(expectedDate,
                responseEmployeeEvaluationQuestionnaireDtoResult.getCreateQuestionnaire());
        assertEquals(expectedScore, responseEmployeeEvaluationQuestionnaireDtoResult.getMiddleScore());
    }

    @Test
    @DisplayName("Получение анкет сотрудников, прошедших оценку")
    void shouldFindEmployeesQuestionnairesAssessedWhenCallRepository() {
        responseEmployeeAssessDto = ResponseEmployeeAssessDto.builder()
                .employeeId(ID_3)
                .questionnaireId(ID_1)
                .evaluatorRole(Role.ROLE_USER)
                .employeePosition("user")
                .employeeFullName("employee3")
                .build();
        responseEmployeeAssessDtoList = new ArrayList<>();
        responseEmployeeAssessDtoList.add(responseEmployeeAssessDto);
        when(employeeService.findByEmail(email2)).thenReturn(evaluated);
        when(employeeEvaluationRepository.findEmployeesQuestionnairesAssessed(ID_3, "",
                startDay.minusDays(5), startDay.plusDays(5)))
                .thenReturn(responseEmployeeAssessDtoList);
        List<ResponseEmployeeAssessDto> responseEmployeeAssessDtoResult = employeeEvaluationService
                .findEmployeesQuestionnairesAssessed(evaluated.getEmail(), "",
                        startDay.minusDays(5), startDay.plusDays(5));
        int expectedSize = 1;
        long expectedEmployeeId = ID_3;
        long expectedQuestionnaireId = ID_1;
        Role expectedEvaluatorRole = Role.ROLE_USER;
        String expectedEmployeePosition = "user";
        String expectedEmployeeFullName = "employee3";
        assertNotNull(responseEmployeeAssessDtoResult);
        assertEquals(expectedSize, responseEmployeeAssessDtoResult.size());
        assertEquals(expectedEmployeeId, responseEmployeeAssessDtoResult.get(0).getEmployeeId());
        assertEquals(expectedQuestionnaireId, responseEmployeeAssessDtoResult.get(0).getQuestionnaireId());
        assertEquals(expectedEvaluatorRole, responseEmployeeAssessDtoResult.get(0).getEvaluatorRole());
        assertEquals(expectedEmployeePosition, responseEmployeeAssessDtoResult.get(0).getEmployeePosition());
        assertEquals(expectedEmployeeFullName, responseEmployeeAssessDtoResult.get(0).getEmployeeFullName());
        verify(employeeEvaluationRepository, times(1))
                .findEmployeesQuestionnairesAssessed(evaluated.getId(), "",
                        startDay.minusDays(5), startDay.plusDays(5));
    }

    @Test
    @DisplayName("Получение списка баллов для анкеты")
    void shouldFindQuestionnaireScores() {
        when(employeeService.findByEmail(email1)).thenReturn(evaluator);
        when(employeeService.findById(ID_3)).thenReturn(evaluated);
        when(questionnaireService.findById(ID_1)).thenReturn(questionnaire);
        when(employeeEvaluationRepository
                .findByEvaluatorIdAndEvaluatedIdAndQuestionnaireId(ID_2, ID_3, ID_1)).thenReturn(employeeEvaluationList);
        when(employeeEvaluationMapper.mapToShortListDto(employeeEvaluationList)).thenReturn(adminEvaluations);
        List<ResponseEmployeeEvaluationShortDto> responseEmployeeEvaluationShortDtoResult = employeeEvaluationService
                .findQuestionnaireScores(email1, questionnaire.getId(), evaluated.getId());
        int expectedSize = 2;
        String expectedName = "employee1";
        double expectedScore = 30.0;
        assertNotNull(responseEmployeeEvaluationShortDtoResult);
        assertEquals(expectedSize, responseEmployeeEvaluationShortDtoResult.size());
        assertEquals(expectedName, responseEmployeeEvaluationShortDtoResult.get(0).getName());
        assertEquals(expectedScore, responseEmployeeEvaluationShortDtoResult.get(1).getScore());
    }

    @Test
    @DisplayName("Получение анкет в которых оценен сотрудник с ID")
    void shouldFindAllQuestionnaireByEvaluatedIdWhenCallRepository(){
        when(employeeEvaluationRepository.findListQuestionnaireByAdminEmailAndEvaluatedId(adminEmail,ID_3,4,
                startDay.minusDays(5),startDay.plusDays(5)))
                .thenReturn(responseEvaluatedQuestionnaireDtoList);
        List<ResponseEvaluatedQuestionnaireDto> responseEvaluatedQuestionnaireDtoListResult =
                employeeEvaluationService.findAllQuestionnaireByEvaluatedId(adminEmail,ID_3,4,
                        startDay.minusDays(5),startDay.plusDays(5));
        int expectedSize = 1;
        long expectedIdQuestionnaire = 1L;
        LocalDate expectedCreateQuestionnaire = startDay;
        double expectedMiddleScore = 10.0;
        assertNotNull(responseEvaluatedQuestionnaireDtoListResult);
        assertEquals(expectedSize, responseEvaluatedQuestionnaireDtoListResult.size());
        assertEquals(expectedIdQuestionnaire,responseEvaluatedQuestionnaireDtoListResult.get(0).getIdQuestionnaire());
        assertEquals(expectedCreateQuestionnaire,responseEvaluatedQuestionnaireDtoListResult.get(0)
                .getCreateQuestionnaire());
        assertEquals(expectedMiddleScore,responseEvaluatedQuestionnaireDtoListResult.get(0).getMiddleScore());
        verify(employeeEvaluationRepository,times(1))
                .findListQuestionnaireByAdminEmailAndEvaluatedId(adminEmail,ID_3,4,
                        startDay.minusDays(5),startDay.plusDays(5));
    }

    @Test
    @DisplayName("Получение списка анкет в которых оценен сотрудник с email")
    void shouldFindAllQuestionnaireByEvaluatedEmailWhenCallRepository(){
        when(employeeService.findByEmail(email2)).thenReturn(evaluated);
        when(employeeEvaluationRepository.findListQuestionnaireByEvaluatedId(ID_3,4,
                startDay.minusDays(5),startDay.plusDays(5)))
                .thenReturn(responseEvaluatedQuestionnaireDtoList);
        List<ResponseEvaluatedQuestionnaireDto> responseEvaluatedQuestionnaireDtoListResult =
                employeeEvaluationService.findAllQuestionnaireByEvaluatedEmail(evaluated.getEmail(),4,
                        startDay.minusDays(5),startDay.plusDays(5));
        int expectedSize = 1;
        long expectedIdQuestionnaire = 1L;
        LocalDate expectedCreateQuestionnaire = startDay;
        double expectedMiddleScore = 10.0;
        assertNotNull(responseEvaluatedQuestionnaireDtoListResult);
        assertEquals(expectedSize, responseEvaluatedQuestionnaireDtoListResult.size());
        assertEquals(expectedIdQuestionnaire,responseEvaluatedQuestionnaireDtoListResult.get(0).getIdQuestionnaire());
        assertEquals(expectedCreateQuestionnaire,responseEvaluatedQuestionnaireDtoListResult.get(0)
                .getCreateQuestionnaire());
        assertEquals(expectedMiddleScore,responseEvaluatedQuestionnaireDtoListResult.get(0).getMiddleScore());
        verify(employeeEvaluationRepository,times(1))
                .findListQuestionnaireByEvaluatedId(evaluated.getId(),4,
                        startDay.minusDays(5),startDay.plusDays(5));
    }

    @Test
    @DisplayName("Получение руководителем персонального рейтинга сотрудника за каждый месяц указанного года")
    void shouldFindPersonalRatingAdminWhenCallRepository(){
        when(employeeService.findByEmail(adminEmail)).thenReturn(admin);
        when(employeeService.findById(ID_3)).thenReturn(evaluated);
        when(employeeEvaluationRepository.findPersonalRatingByAdmin(ID_3,2024))
                .thenReturn(responseRatingFullDtoList);
        List<ResponseRatingFullDto> responseRatingFullDtoListResult = employeeEvaluationService
                .findPersonalRatingAdmin(admin.getEmail(),evaluated.getId(), 2024);
        int expectedSize = 1;
        double expectedRating = 10.0;
        int expectedMonthNumber = startDay.getMonthValue();
        assertNotNull(responseRatingFullDtoListResult);
        assertEquals(expectedSize, responseRatingFullDtoListResult.size());
        assertEquals(expectedRating,responseRatingFullDtoListResult.get(0).getRating());
        verify(employeeEvaluationRepository,times(1))
                .findPersonalRatingByAdmin(evaluated.getId(),2024);
    }

    @Test
    @DisplayName("Получение сотрудником своего среднего рейтинга за текущий месяц")
    void shouldFindAverageRatingByUserWhenCallRepository(){
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return evaluated.getEmail();
            }
        };
        when(employeeService.findByEmail(email2)).thenReturn(evaluated);
        when(employeeEvaluationRepository.getAverageRatingByEvaluatedIdAndCurrentMonth(ID_3,
                startDay.minusDays(5),startDay.plusDays(5))).thenReturn(10.0);
        double averageRatingByUserResult = employeeEvaluationService.findAverageRatingByUser(principal,
                startDay.minusDays(5),startDay.plusDays(5));
        double expectedRating = 10.0;
        assertNotNull(averageRatingByUserResult);
        assertEquals(expectedRating,averageRatingByUserResult);
        verify(employeeEvaluationRepository,times(1))
                .getAverageRatingByEvaluatedIdAndCurrentMonth(evaluated.getId(),
                        startDay.minusDays(5),startDay.plusDays(5));
    }

    @Test
    @DisplayName("Получение админом среднего рейтинга сотрудника за текущий месяц")
    void shouldFindAverageRatingByAdminWhenCallRepository(){
        when(employeeService.findById(ID_3)).thenReturn(evaluated);
        when(employeeService.findByEmail(adminEmail)).thenReturn(admin);
        when(employeeEvaluationRepository.getAverageRatingByEvaluatedIdAndCurrentMonth(ID_3,
                startDay.minusDays(5),startDay.plusDays(5))).thenReturn(10.0);
        double averageRatingByAdminResult = employeeEvaluationService.findAverageRatingByAdmin(admin.getEmail(),
                evaluated.getId(),startDay.minusDays(5),startDay.plusDays(5));
        double expectedRating = 10.0;
        assertNotNull(averageRatingByAdminResult);
        assertEquals(expectedRating,averageRatingByAdminResult);
        verify(employeeEvaluationRepository,times(1))
                .getAverageRatingByEvaluatedIdAndCurrentMonth(evaluated.getId(),
                        startDay.minusDays(5),startDay.plusDays(5));
    }
}
