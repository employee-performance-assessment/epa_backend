package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.questionnaire.RequestQuestionnaireDto;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
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
import java.util.Objects;
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

        Optional<Questionnaire> lastQuestionnaire = questionnaireRepository.findFirstByAuthorEmailOrderByIdDesc(email);
        if (lastQuestionnaire.isEmpty()) {
            throw new BadRequestException("Необходимо создать заранее анкету для возможности редактирования");
        } else if (QuestionnaireStatus.SHARED.equals(lastQuestionnaire.get().getStatus())) {
            throw new BadRequestException("Невозможно обновить разосланную анкету. " +
                    "Сперва воспользуйтесь получением последней анкеты");
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
                new EntityNotFoundException("Анкета не найдена"));
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
            throw new BadRequestException("Невозможно посмотреть анкету. Она создана не вашим руководителем");
        }
        if (QuestionnaireStatus.CREATED.equals(questionnaire.getStatus())) {
            throw new BadRequestException("Запрашиваемая анкета еще не разослана руководителем");
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

    @Override
    public Questionnaire updateLastWithDefault(String email) {
        Questionnaire questionnaire = findLastByAuthorEmail(email);
        questionnaire.setCriterias(criteriaService.findDefault());
        questionnaire.setCreated(LocalDate.now());
        return questionnaireRepository.save(questionnaire);
    }

    @Override
    public void checkAdminForQuestionnaire(Employee admin, Questionnaire questionnaire) {
        Long adminId = admin.getId();
        Long authorId = questionnaire.getAuthor().getId();
        if (!Objects.equals(adminId, authorId)) {
            throw new BadRequestException(String.format("Руководитель %s не является автором запрашиваемой анкеты",
                    admin.getFullName()));
        }
    }
}