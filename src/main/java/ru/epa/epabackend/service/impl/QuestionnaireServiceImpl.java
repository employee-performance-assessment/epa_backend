package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireRequestDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.repository.QuestionnaireRepository;
import ru.epa.epabackend.service.CriteriaService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс QuestionnaireServieImpl содержит логику работы с анкетами
 */
@Service
@RequiredArgsConstructor
@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final EmployeeService employeeService;
    private final QuestionnaireMapper questionnaireMapper;
    private final CriteriaService criteriaService;

    /**
     * Получение самой последней анкеты админа с указанным статусом
     */
    @Override
    public Questionnaire findLastByAuthorAndStatus(String email, QuestionnaireStatus status) {
        Employee author = employeeService.findByEmail(email).getCreator();
        String authorEmail = author == null ? email : author.getEmail();
        return questionnaireRepository.findFirstByAuthorEmailAndStatusOrderByIdDesc(authorEmail, status).orElseThrow(() ->
                new EntityNotFoundException(String.format("Анкеты для администратора с email %s и статусом %s не найдена",
                        email, status)));
    }

    /**
     * Получение самой последней анкеты админа по email
     */
    @Override
    public Questionnaire findLastByAuthorEmail(String email) {
        return questionnaireRepository.findFirstByAuthorEmailOrderByIdDesc(email).orElseThrow(() ->
                new EntityNotFoundException(String.format("Анкеты администратора с email %s не найдено", email)));
    }

    /**
     * Сохранение анкеты, имея список критериев и email админа
     */
    @Override
    public Questionnaire save(QuestionnaireRequestDto questionnaireRequestDto, String email) {
        Employee author = employeeService.findByEmail(email);
        Optional<Questionnaire> lastQuestionnaire = questionnaireRepository.findFirstByAuthorEmailOrderByIdDesc(email);
        if (lastQuestionnaire.isPresent() && lastQuestionnaire.get().getStatus() == QuestionnaireStatus.CREATED) {
            throw new BadRequestException(String.format("Возможно создать анкету только если ваша последняя анкета " +
                    "имела статус SHARE. Воспользуйтесь обновлением анкеты."));
        }
        Set<Criteria> criterias = criteriaService.findExistentAndSaveNonExistentCriterias(questionnaireRequestDto.getCriterias());
        Questionnaire questionnaire = questionnaireMapper.mapToEntity(criterias, author, QuestionnaireStatus.CREATED);
        return questionnaireRepository.save(questionnaire);
    }

    /**
     * Редактирование (обновление) анкеты, имея список критериев и email админа
     */
    @Override
    public Questionnaire updateLast(QuestionnaireRequestDto questionnaireRequestDto, String email) {
        Questionnaire questionnaire = findLastByAuthorEmail(email);
        Employee author = employeeService.findByEmail(email);
        if (questionnaire.getStatus() != QuestionnaireStatus.CREATED) {
            throw new BadRequestException(String.format("Анкета с id %d и статусом %s не может быть обновлена. " +
                    "Необходимо создать новую анкету", questionnaire.getId(), questionnaire.getStatus()));
        }
        Set<Criteria> criterias = criteriaService.findExistentAndSaveNonExistentCriterias(questionnaireRequestDto.getCriterias());
        return questionnaireRepository.save(questionnaireMapper.mapToEntity(criterias, author, QuestionnaireStatus.CREATED,
                questionnaire.getId()));
    }

    /**
     * Получение анкеты по её id
     */
    @Override
    public Questionnaire findById(long id) {
        return questionnaireRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Анкета с id %d не найдена", id)));
    }

    /**
     * Обновление статуса анкеты с CREATE на SHARED и выставление сегодняшней даты
     */
    @Override
    public Questionnaire updateLastQuestionnaireStatusAndDate(QuestionnaireStatus status, String email) {
        Questionnaire questionnaire = findLastByAuthorEmail(email);
        if (questionnaire.getStatus() != QuestionnaireStatus.CREATED) {
            throw new BadRequestException("Для изменения статуса последней анкеты на SHARED, необходимо, чтобы " +
                    "она имела статус CREATED.");
        }
        questionnaire.setStatus(QuestionnaireStatus.SHARED);
        questionnaire.setCreated(LocalDate.now());
        return questionnaireRepository.save(questionnaire);
    }

    /**
     * Сохранение дубликата опубликованной (SHARED) анкеты для повторного анкетирования среди сотрудников
     * в другую дату по тем же критериям
     */
    @Override
    public Questionnaire duplicateLastShared(String email) {
        Questionnaire lastQuestionnaire = findLastByAuthorEmail(email);
        if (lastQuestionnaire.getStatus() != QuestionnaireStatus.SHARED)
            throw new BadRequestException(String.format("Анкета может быть продублирована только при статусе SHARE " +
                    "последней анкеты."));
        Questionnaire newQuestionnaire = Questionnaire.builder()
                .author(lastQuestionnaire.getAuthor())
                .created(LocalDate.now())
                .status(lastQuestionnaire.getStatus())
                .criterias(new LinkedHashSet<>(lastQuestionnaire.getCriterias()))
                .build();
        return questionnaireRepository.save(newQuestionnaire);
    }

    /**
     * Сохранение анктеты с дефолтными критериями (по умолчанию) со статусом SHARED. Это возможно, когда админ не
     * создавал анкет, но хочет провести анкетирование
     */
    @Override
    public Questionnaire saveDefaultWithSharedStatus(String email) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findFirstByAuthorEmailOrderByIdDesc(email);
        if (questionnaire.isPresent()) {
            throw new BadRequestException("Для сохранения дефолтного списка критерией со статусом анкеты SHARED " +
                    "необходимо, чтобы у админа не было анкет");
        }
        Employee author = employeeService.findByEmail(email);
        Set<Criteria> defaultCriterias = new LinkedHashSet<>(criteriaService.findDefault());
        Questionnaire newQuestionnaire = Questionnaire.builder()
                .author(author)
                .status(QuestionnaireStatus.SHARED)
                .created(LocalDate.now())
                .criterias(defaultCriterias)
                .build();
        return questionnaireRepository.save(newQuestionnaire);
    }

    /**
     * Получение анкеты админа по id анкеты и по email сотрудника или администратора
     */
    @Override
    public Questionnaire findByEmailAndId(String email, long questionnaireId) {
        Employee employee = employeeService.findByEmail(email);
        Employee author = employee.getCreator();
        Long authorId = author == null ? employee.getId() : author.getId();
        Questionnaire questionnaire = findById(questionnaireId);
        if (!Objects.equals(questionnaire.getAuthor().getId(), authorId)) {
            throw new BadRequestException("Чтобы иметь доступ к анкете, необходимо, чтобы " +
                    "ваш администратор был её автором");
        }
        if (questionnaire.getStatus().equals(QuestionnaireStatus.CREATED)) {
            throw new BadRequestException(String.format("Администратор еще не опубликовал анкету. " +
                    "Запрашиваемая анкета c id %d имеет статус %s", questionnaireId, questionnaire.getStatus()));
        }
        return questionnaire;
    }

    /**
     * Получение всех анкет админа c определенным статусом любым сотрудником
     */
    @Override
    public List<Questionnaire> findAllByAuthorIdAndStatus(String email, QuestionnaireStatus status) {
        Employee employee = employeeService.findByEmail(email);
        Employee author = employee.getCreator();
        Long authorId = author == null ? employee.getId() : author.getId();
        return questionnaireRepository.findAllByAuthorIdAndStatus(authorId, status);
    }
}