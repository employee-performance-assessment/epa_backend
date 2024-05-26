package ru.epa.epabackend.questionnaire;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;
import ru.epa.epabackend.dto.questionnaire.RequestQuestionnaireDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.repository.QuestionnaireRepository;
import ru.epa.epabackend.service.CriteriaService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.impl.QuestionnaireServiceImpl;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionnaireUnitTests {
    private final long ID_1 = 1L;
    private final long ID_2 = 2L;
    private final long ID_3 = 3L;
    private final String email1 = "qwerty1@gmail.com";
    private final String email2 = "qwerty2@gmail.com";
    private final LocalDate created = LocalDate.now();
    @Mock
    private QuestionnaireRepository questionnaireRepository;
    @InjectMocks
    private QuestionnaireServiceImpl questionnaireService;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private CriteriaService criteriaService;
    private Employee admin;
    private Employee author;
    List<Criteria> criterias = new ArrayList<>();
    List<RequestCriteriaDto> criteriasDto = new ArrayList<>();
    private Questionnaire questionnaire1;
    private Questionnaire questionnaire2;
    private Criteria criteria;
    private RequestCriteriaDto requestCriteriaDto;
    Optional<Questionnaire> lastQuestionnaire;
    private RequestQuestionnaireDto requestQuestionnaireDto;
    private Set<String> uniqueCriterias;

    @BeforeEach
    public void unit() {
        admin = Employee.builder()
                .id(ID_1)
                .email(email1)
                .build();
        author = Employee.builder()
                .id(ID_2)
                .email(email2)
                .creator(admin)
                .build();
        criteria = Criteria.builder()
                .id(ID_1)
                .name("criteria")
                .build();
        criterias.add(criteria);
        questionnaire1 = Questionnaire.builder()
                .id(ID_1)
                .author(admin)
                .created(created)
                .criterias(criterias)
                .status(QuestionnaireStatus.CREATED)
                .build();
        lastQuestionnaire = Optional.of(questionnaire1);
        requestCriteriaDto = RequestCriteriaDto.builder()
                .name("criteriaDto")
                .build();
        criteriasDto.add(requestCriteriaDto);
        requestQuestionnaireDto = RequestQuestionnaireDto.builder()
                .criterias(criteriasDto)
                .build();
    }

    @Test
    @DisplayName("Получение последней анкеты админа по email" +
            " Если есть анкета со статусом CREATED, то возвращаем её")
    void shouldFindLastByAuthorEmailWhenQuestionnaireHaveStatusCreated() {
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email1)).thenReturn(lastQuestionnaire);
        questionnaire1 = lastQuestionnaire.get();
        Questionnaire questionnaireResult = questionnaireService.findLastByAuthorEmail(email1);
        int expectedId = 1;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }

    @Test
    @DisplayName("Получение последней анкеты админа по email" +
            " У прошлой анкеты был статус SHARED, поэтому создаётся новая анкета со статусом CREATED")
    void shouldFindLastByAuthorEmailWhenQuestionnaireHaveStatusSHARED() {
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email1)).thenReturn(lastQuestionnaire);
        questionnaire2 = lastQuestionnaire.get();
        questionnaire2.setStatus(QuestionnaireStatus.SHARED);
        when(employeeService.findByEmail(email1)).thenReturn(admin);
        when(questionnaireService.saveWithParameters(QuestionnaireStatus.CREATED, admin, criterias))
                .thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService.findLastByAuthorEmail(email1);
        int expectedId = 1;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }

    @Test
    @DisplayName("Получение последней анкеты админа по email" +
            " У админа не было анкет, поэтому создаётся новая анкета со статусом CREATED и дефолтными критериями")
    void shouldFindLastByAuthorEmailWhenAdminDontHaveQuestionnaire() {
        lastQuestionnaire = Optional.empty();
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email2)).thenReturn(lastQuestionnaire);
        when(employeeService.findByEmail(email2)).thenReturn(author);
        when(questionnaireService.saveWithParameters(QuestionnaireStatus.CREATED, admin, criterias))
                .thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService.findLastByAuthorEmail(email2);
        int expectedId = 1;
        assertEquals(expectedId, questionnaireResult.getId());
    }

    @Test
    @DisplayName("Сохранение анкеты с параметрами")
    void shouldSaveWithParametersWhenCallRepository() {
        questionnaire2 = Questionnaire.builder()
                .status(QuestionnaireStatus.CREATED)
                .created(created)
                .author(admin)
                .criterias(criterias)
                .build();
        when(questionnaireRepository.save(questionnaire2)).thenReturn(questionnaire1);
        when(questionnaireService.saveWithParameters(QuestionnaireStatus.CREATED, admin, criterias))
                .thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService
                .saveWithParameters(questionnaire1.getStatus(), questionnaire1.getAuthor(), questionnaire1.getCriterias());
        int expectedId = 1;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }

    @Test
    @DisplayName("Обновление анкеты, имея анкету и email админа")
    void shouldUpdateLastWhenCallRepository() {
        uniqueCriterias = new HashSet<>();
        uniqueCriterias.add("criteriaDto");
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email1)).thenReturn(lastQuestionnaire);
        when(criteriaService.findExistentAndSaveNonExistentCriterias(uniqueCriterias)).thenReturn(criterias);
        when(questionnaireRepository.save(questionnaire1)).thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService.updateLast(requestQuestionnaireDto, email1);
        int expectedId = 1;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }

    @Test
    @DisplayName("Обновление анкеты, имея анкету и email админа, когда" +
            " lastQuestionnaire.isEmpty")
    void shouldUpdateLastWhenLastQuestionnaireIsEmpty() {
        lastQuestionnaire = Optional.empty();
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email1)).thenReturn(lastQuestionnaire);
        assertThrows(BadRequestException.class, () -> questionnaireService.updateLast(requestQuestionnaireDto, email1));
    }

    @Test
    @DisplayName("Обновление анкеты, имея анкету и email админа, когда" +
            " lastQuestionnaire имеет статус Shared")
    void shouldUpdateLastWhenLastQuestionnaireStatusIsShared() {
        questionnaire1.setStatus(QuestionnaireStatus.SHARED);
        lastQuestionnaire = Optional.of(questionnaire1);
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email1)).thenReturn(lastQuestionnaire);
        assertThrows(BadRequestException.class, () -> questionnaireService.updateLast(requestQuestionnaireDto, email1));
    }

    @Test
    @DisplayName("Получение анкеты по id с исключением Not Found Exception")
    void shouldFindByIdEmployeeWhenThrowNotFoundException() throws ValidationException {
        when(questionnaireRepository.findById(ID_2)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> questionnaireService.findById(ID_2));
    }

    @Test
    @DisplayName("Получение анкеты по id с вызовом репозитория")
    void shouldFindByIdEmployeeWhenCallRepository() {
        when(questionnaireRepository.findById(ID_1)).thenReturn(Optional.ofNullable(questionnaire1));
        Questionnaire questionnaireResult = questionnaireService.findById(questionnaire1.getId());
        long expectedId = 1L;
        assertEquals(expectedId, questionnaireResult.getId());
        verify(questionnaireRepository, times(1)).findById(questionnaireResult.getId());
    }

    @Test
    @DisplayName("Отправление анкеты сотрудникам - изменение статуса последней анкеты с CREATED на SHARED")
    void shouldSendQuestionnaireToEmployeesWhenCallRepositoryQuestionnaireStatusToShared() {
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email1)).thenReturn(lastQuestionnaire);
        questionnaire1.setStatus(QuestionnaireStatus.SHARED);
        when(questionnaireService.sendQuestionnaireToEmployees(email1)).thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService.sendQuestionnaireToEmployees(email1);
        long expectedId = 1L;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());

    }

    @Test
    @DisplayName("Последняя анкета имела статус SHARED, поэтому создаётся дубликат анкеты с новым id и датой " +
            "и статусом SHARED")
    void shouldSendQuestionnaireToEmployeesWhenQuestionnaireHaveStatusShared() {
        when(questionnaireRepository.findFirstByAuthorEmailOrderByCreatedDescIdDesc(email1)).thenReturn(lastQuestionnaire);
        when(employeeService.findByEmail(email1)).thenReturn(admin);
        questionnaire1.setStatus(QuestionnaireStatus.SHARED);
        when(questionnaireService
                .saveWithParameters(QuestionnaireStatus.SHARED, admin, criterias)).thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService.sendQuestionnaireToEmployees(email1);
        long expectedId = 1L;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }

    @Test
    @DisplayName("Отправление анкеты сотрудникам. " +
            "У админа нет анкет, поэтому создаётся анкета с дефолтными критериями и статусом SHARED")
    void shouldSendQuestionnaireToEmployeesWhenAdminDintHaveQuestionnaire() {
        lastQuestionnaire = Optional.empty();
        when(employeeService.findByEmail(email1)).thenReturn(admin);
        when(criteriaService.findDefault()).thenReturn(criterias);
        questionnaire1.setStatus(QuestionnaireStatus.SHARED);
        when(questionnaireService
                .saveWithParameters(QuestionnaireStatus.SHARED, admin, criterias)).thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService.sendQuestionnaireToEmployees(email1);
        long expectedId = 1L;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }


    @Test
    @DisplayName("Получение анкеты админа по id анкеты и по email сотрудника или админа")
    void shouldFindByEmailAndIdWhenReturnQuestionnaire() {
        when(employeeService.findByEmail(email1)).thenReturn(admin);
        questionnaire1.setStatus(QuestionnaireStatus.SHARED);//убрать для 2 варианта
        when(questionnaireRepository.findById(ID_1)).thenReturn(Optional.of(questionnaire1));
        Questionnaire questionnaireResult = questionnaireService.findByEmailAndId(email1, questionnaire1.getId());
        long expectedId = 1L;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }

    @Test
    @DisplayName("Получение анкеты админа по id анкеты и по email сотрудника или админа " +
            "с исключением BadRequestException")
    void shouldFindByEmailAndIdWhenReturnBadRequestExceptionWhenAdminNotAuthor() {
        when(employeeService.findByEmail(email2)).thenReturn(author);
        when(questionnaireRepository.findById(ID_1)).thenReturn(Optional.of(questionnaire1));
        assertThrows(BadRequestException.class, () -> questionnaireService.findByEmailAndId(email2, ID_1));
    }

    @Test
    @DisplayName("Получение анкеты админа по id анкеты и по email сотрудника или админа " +
            "с исключением BadRequestException")
    void shouldFindByEmailAndIdWhenReturnBadRequestExceptionWhenQuestionnaireHaveStatusCreated() {
        when(employeeService.findByEmail(email1)).thenReturn(admin);
        when(questionnaireRepository.findById(ID_1)).thenReturn(Optional.of(questionnaire1));
        assertThrows(BadRequestException.class, () -> questionnaireService.findByEmailAndId(email1, ID_1));
    }

    @Test
    @DisplayName("Получение всех анкет админа с определенным статусом любым сотрудником")
    void shouldFindAllByAuthorIdAndStatusWhenCallRepository() {
        when(employeeService.findByEmail(email1)).thenReturn(admin);
        when(questionnaireRepository.findAllByAuthorIdAndStatus(ID_1, QuestionnaireStatus.CREATED))
                .thenReturn(List.of(questionnaire1));
        List<Questionnaire> questionnaireListResult = questionnaireService
                .findAllByAuthorIdAndStatus(admin.getEmail(), QuestionnaireStatus.CREATED);
        int expectedSize = 1;
        assertNotNull(questionnaireListResult);
        assertEquals(expectedSize, questionnaireListResult.size());
        verify(questionnaireRepository, times(1))
                .findAllByAuthorIdAndStatus(admin.getId(), questionnaire1.getStatus());
    }

    @Test
    @DisplayName("")
    void shouldUpdateLastWithDefaultWhenCallRepository() {
        when(questionnaireService.findLastByAuthorEmail(email1)).thenReturn(questionnaire1);
        questionnaire1.setCreated(LocalDate.now());
        when(questionnaireService.updateLastWithDefault(email1)).thenReturn(questionnaire1);
        Questionnaire questionnaireResult = questionnaireService.updateLastWithDefault(email1);
        long expectedId = 1L;
        assertNotNull(questionnaireResult);
        assertEquals(expectedId, questionnaireResult.getId());
    }
}
