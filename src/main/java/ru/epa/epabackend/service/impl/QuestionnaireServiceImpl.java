package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.questionnaire.RequestQuestionnaireDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.exception.exceptions.ConflictException;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.repository.QuestionnaireRepository;
import ru.epa.epabackend.service.CriteriaService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.QuestionnaireService;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс QuestionnaireServiceImpl содержит логику работы с анкетами
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final EmployeeService employeeService;
    private final CriteriaService criteriaService;

    /**
     * Получение самой последней анкеты админа с указанным статусом
     */
    @Override
    @Transactional(readOnly = true)
    public Questionnaire findLastByAuthorAndStatus(String email, QuestionnaireStatus status) {
        log.info("Получение самой последней анкеты админа с указанным статусом {}", status);
        Employee author = employeeService.findByEmail(email).getCreator();
        String authorEmail = author == null ? email : author.getEmail();
        return questionnaireRepository.findFirstByAuthorEmailAndStatusOrderByIdDesc(authorEmail, status)
                .orElseThrow(() -> new EntityNotFoundException(String
                        .format("Анкеты для администратора с email %s и статусом %s не найдена", email, status)));
    }

    /**
     * Получение последней анкеты админа по email
     * Если есть анкета со статусом CREATED, то возвращаем её
     * Если есть анкета со статусом SHARED, то создаём новую анкету со статусом CREATED и возвращаем её
     * Если нет анкет, то создаём анкету с дефолтными критериями и статусом CREATED
     */
    @Override
    public Questionnaire findLastByAuthorEmail(String email) {
        log.info("Получение самой последней анкеты админа по email");
        Optional<Questionnaire> lastQuestionnaire = questionnaireRepository.findFirstByAuthorEmailOrderByIdDesc(email);
        if (lastQuestionnaire.isPresent()) {
            Questionnaire questionnaire = lastQuestionnaire.get();
            if (QuestionnaireStatus.CREATED.equals(questionnaire.getStatus())) {
                return questionnaire;
            } else {
                log.info("У прошлой анкеты был статус SHARED, поэтому создаётся новая анкета со статусом CREATED");
                Employee author = employeeService.findByEmail(email);
                List<Criteria> criterias = questionnaire.getCriterias();
                return saveWithParameters(QuestionnaireStatus.CREATED, author, criterias);
            }
        } else {
            log.info("У админа не было анкет, поэтому создаётся новая анкета со статусом CREATED и дефолтными критериями");
            Employee author = employeeService.findByEmail(email);
            return saveWithParameters(QuestionnaireStatus.CREATED, author, criteriaService.findDefault());
        }
    }

    @Override
    public Questionnaire saveWithParameters(QuestionnaireStatus status, Employee author, List<Criteria> criterias) {
        return questionnaireRepository.save(Questionnaire.builder()
                .status(status)
                .created(LocalDate.now())
                .author(author)
                .criterias(new ArrayList<>(criterias))
                .build());
    }

    /**
     * Обновление анкеты, имея анкету и email админа
     */
    @Override
    public Questionnaire updateLast(RequestQuestionnaireDto requestQuestionnaireDto, String email) {
        log.info("Обновление анкеты");

        long questionnaireId = requestQuestionnaireDto.getId();
        Optional<Questionnaire> lastQuestionnaire = questionnaireRepository.findFirstByAuthorEmailOrderByIdDesc(email);
        if (lastQuestionnaire.isEmpty()) {
            throw new BadRequestException("Необходимо создать заранее анкету для возможности редактирования");
        } else if (questionnaireId != lastQuestionnaire.get().getId()) {
            throw new ConflictException(String.format("Передаваемая анкета с id %d не совпадает с id последней анкеты " +
                    "%d", questionnaireId, lastQuestionnaire.get().getId()));
        } else if (QuestionnaireStatus.SHARED.equals(lastQuestionnaire.get().getStatus())) {
            throw new BadRequestException("Невозможно обновить анкету со статусом SHARED. Воспользуйтесь " +
                    "получением последней анкеты со статусом CREATED.");
        }

        List<Criteria> criterias = criteriaService.findExistentAndSaveNonExistentCriterias(requestQuestionnaireDto
                .getCriterias());
        Questionnaire questionnaire = lastQuestionnaire.get();
        questionnaire.setCriterias(criterias);
        questionnaire.setCreated(LocalDate.now());
        return questionnaireRepository.save(questionnaire);
    }

    /**
     * Получение анкеты по её id
     */
    @Override
    @Transactional(readOnly = true)
    public Questionnaire findById(long id) {
        log.info("Получение анкеты по её идентификатору {}", id);
        return questionnaireRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Анкета с id %d не найдена", id)));
    }

    /**
     * Отправление анкеты сотрудникам - изменение статуса последней анкеты с CREATED на SHARED
     */
    @Override
    public Questionnaire sendQuestionnaireToEmployees(String email) {
        log.info("Отправление анкеты сотрудникам");
        Optional<Questionnaire> lastQuestionnaire = questionnaireRepository.findFirstByAuthorEmailOrderByIdDesc(email);
        if (lastQuestionnaire.isPresent()) {
            Questionnaire questionnaire = lastQuestionnaire.get();
            if (QuestionnaireStatus.CREATED.equals(questionnaire.getStatus())) {
                log.info("Изменение статуса последней анкеты с CREATED на SHARED");
                questionnaire.setStatus(QuestionnaireStatus.SHARED);
                questionnaire.setCreated(LocalDate.now());
                return questionnaireRepository.save(questionnaire);
            } else {
                log.info("Последняя анкета имела статус SHARED, поэтому создаётся дубликат анкеты с новым id и датой " +
                        "и статусом SHARED");
                Employee author = employeeService.findByEmail(email);
                List<Criteria> criterias = questionnaire.getCriterias();
                return saveWithParameters(QuestionnaireStatus.SHARED, author, criterias);
            }
        } else {
            log.info("У админа нет анкет, поэтому создаётся анкета с дефолтными критериями и статусом SHARED");
            Employee author = employeeService.findByEmail(email);
            List<Criteria> criterias = criteriaService.findDefault();
            return saveWithParameters(QuestionnaireStatus.SHARED, author, criterias);
        }
    }

    /**
     * Получение анкеты админа по id анкеты и по email сотрудника или администратора
     */
    @Override
    @Transactional(readOnly = true)
    public Questionnaire findByEmailAndId(String email, long questionnaireId) {
        log.info("Получение анкеты админа по идентификатору анкеты {} и по email сотрудника или администратора",
                questionnaireId);
        Employee employee = employeeService.findByEmail(email);
        Employee author = employee.getCreator();
        Long authorId = author == null ? employee.getId() : author.getId();
        Questionnaire questionnaire = findById(questionnaireId);
        if (!authorId.equals(questionnaire.getAuthor().getId())) {
            throw new BadRequestException("Чтобы иметь доступ к анкете, необходимо, чтобы " +
                    "ваш администратор был её автором");
        }
        if (QuestionnaireStatus.CREATED.equals(questionnaire.getStatus())) {
            throw new BadRequestException(String.format("Администратор еще не опубликовал анкету. " +
                    "Запрашиваемая анкета c id %d имеет статус %s", questionnaireId, questionnaire.getStatus()));
        }
        return questionnaire;
    }

    /**
     * Получение всех анкет админа с определенным статусом любым сотрудником
     */
    @Override
    @Transactional(readOnly = true)
    public List<Questionnaire> findAllByAuthorIdAndStatus(String email, QuestionnaireStatus status) {
        log.info("Получение всех анкет админа с определенным статусом {} любым сотрудником", status);
        Employee employee = employeeService.findByEmail(email);
        Employee author = employee.getCreator();
        Long authorId = author == null ? employee.getId() : author.getId();
        return questionnaireRepository.findAllByAuthorIdAndStatus(authorId, status);
    }

    /**
     * Получение флага true/false прошёл ли день с последней отправки анкеты
     */
    @Override
    public boolean isDayPassedAfterShareQuestionnaire(String email) {
        log.info("Получение флага true/false прошёл ли день с последней отправки анкеты");
        Optional<Questionnaire> lastQuestionnaire = questionnaireRepository
                .findFirstByAuthorEmailAndStatusOrderByIdDesc(email, QuestionnaireStatus.SHARED);
        if (lastQuestionnaire.isEmpty()) return true;
        Questionnaire questionnaire = lastQuestionnaire.get();
        return questionnaire.getCreated().isBefore(LocalDate.now());
    }
}