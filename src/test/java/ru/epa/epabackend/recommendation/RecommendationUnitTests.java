package ru.epa.epabackend.recommendation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.repository.RecommendationRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.impl.RecommendationServiceImpl;
import ru.epa.epabackend.util.Role;

import java.time.LocalDate;

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
    private Recommendation recommendation;
    private String stringRecommendation;
    private Employee recipient;
    protected Employee sender;

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
        stringRecommendation = "recommendationDto";
    }

   /* @Test
    @DisplayName("Создание технологии с вызовом репозитория")
    void shouldCreateWhenCallRepository() {
        when(employeeService.findByEmail(email_2)).thenReturn(recipient);
        when(employeeService.findByEmail(email_1)).thenReturn(sender);
        when(recommendationMapper.mapToEntity(requestRecommendationDto,recipient,sender)).thenReturn(recommendation);
        when(recommendationRepository.save(recommendation)).thenReturn(recommendation);
        Recommendation recommendationResult = recommendationService.create(requestRecommendationDto,email_2,email_1);
        int expectedId = 1;
        assertNotNull(recommendationResult);
        assertEquals(expectedId,recommendationResult.getId());
        verify(recommendationRepository,times(1)).save(recommendationResult);
    }*/

   /* @Test
    @DisplayName("Получение списка рекомендаций для сотрудника с ID")
    void shouldFindAllByRecipientEmailWhenCallRepository() {
        when(recommendationRepository.findAllByRecipientEmail(email_2)).thenReturn(List.of(recommendation));
        List<Recommendation> recommendationResult = recommendationService.findAllByRecipientEmail(recipient.getEmail());
        int expectedSize = 1;
        assertNotNull(recommendationResult);
        assertEquals(expectedSize,recommendationResult.size());
        verify(recommendationRepository,times(1)).findAllByRecipientEmail(recipient.getEmail());
    }*/
}
