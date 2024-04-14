package ru.epa.epabackend.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.repository.RecommendationRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.service.impl.RecommendationServiceImpl;
import ru.epa.epabackend.util.QuestionnaireStatus;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final String email_1 = "qwerty1@gmail.com";
    private static final String email_2 = "qwerty2@gmail.com";
    @Mock
    private RecommendationRepository recommendationRepository;
    @InjectMocks
    private RecommendationServiceImpl recommendationService;
    @Mock
    private RecommendationMapper recommendationMapper;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private QuestionnaireService questionnaireService;
    private Recommendation recommendation;
    private Employee recipient;
    private Employee sender;
    private Questionnaire questionnaire;


    @BeforeEach
    public void unit() {
        sender = Employee.builder()
                .id(ID_1)
                .email(email_1)
                .role(Role.ROLE_ADMIN)
                .build();
        recipient = Employee.builder()
                .id(ID_2)
                .email(email_2)
                .role(Role.ROLE_USER)
                .build();
        recommendation = Recommendation.builder()
                .id(ID_1)
                .recipient(recipient)
                .sender(sender)
                .recommendation("recommendation")
                .createDay(LocalDate.now())
                .build();
        questionnaire = Questionnaire.builder()
                .id(ID_1)
                .status(QuestionnaireStatus.CREATED)
                .build();
    }

    @Test
    @DisplayName("Создание рекомендации с вызовом репозитория")
    void shouldCreateWhenCallRepository() {
        when(employeeService.findById(ID_2)).thenReturn(recipient);//recipient - evaluated
        when(employeeService.findByEmail(email_1)).thenReturn(sender);
        when(questionnaireService.findById(ID_1)).thenReturn(questionnaire);
        when(recommendationMapper.mapToEntity("recommendation", questionnaire, recipient, sender))
                .thenReturn(recommendation);
        when(recommendationRepository.save(recommendation)).thenReturn(recommendation);
        Recommendation recommendationResult = recommendationService
                .create(recommendation.getRecommendation(), questionnaire.getId(), recipient.getId(), sender.getEmail());
        int expectedId = 1;
        assertNotNull(recommendationResult);
        assertEquals(expectedId, recommendationResult.getId());
        verify(recommendationRepository, times(1)).save(recommendationResult);
    }

    @Test
    @DisplayName("Получение рекомендации по Id сотрудника, получившего рекомендацию и Id анкеты")
    void shouldGetByRecipientIdAndQuestionnaireIdWhenCallRepository() {
        when(recommendationRepository.getByRecipientIdAndQuestionnaireId(ID_2, ID_1)).thenReturn(recommendation);
        Recommendation recommendationResult = recommendationService
                .getByRecipientIdAndQuestionnaireId(recipient.getId(), questionnaire.getId());
        long expectedId = 1L;
        assertEquals(expectedId, recommendationResult.getId());
        verify(recommendationRepository, times(1))
                .getByRecipientIdAndQuestionnaireId(recipient.getId(), questionnaire.getId());
    }
}
