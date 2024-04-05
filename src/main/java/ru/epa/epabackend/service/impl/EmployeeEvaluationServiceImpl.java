package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.dto.recommendation.ResponseRecommendationShortDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.repository.RecommendationRepository;
import ru.epa.epabackend.service.CriteriaService;
import ru.epa.epabackend.service.EmployeeEvaluationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.QuestionnaireService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс EmployeeEvaluationServiceImpl содержит бизнес-логику работы с оценками сотрудников своих коллег.
 *
 * @author Михаил Безуглов
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeEvaluationServiceImpl implements EmployeeEvaluationService {

    private final EmployeeEvaluationRepository employeeEvaluationRepository;
    private final RecommendationRepository recommendationRepository;
    private final EmployeeEvaluationMapper employeeEvaluationMapper;
    private final EmployeeMapper employeeMapper;
    private final RecommendationMapper recommendationMapper;
    private final EmployeeService employeeService;
    private final CriteriaService criteriaService;
    private final QuestionnaireService questionnaireService;

    /**
     * Сохранение оценки.
     */
    @Override
    public List<EmployeeEvaluation> create(String email, Long evaluatedId, Long questionnaireId,
                                           List<RequestEmployeeEvaluationDto> evaluationRequestDtoList) {
        log.info("Сохранение оценки");
        Employee evaluated = employeeService.findById(evaluatedId);
        Employee evaluator = employeeService.findByEmail(email);
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        List<EmployeeEvaluation> employeeEvaluations = new ArrayList<>(evaluationRequestDtoList.size());

        for (RequestEmployeeEvaluationDto evaluationRequestDto : evaluationRequestDtoList) {
            Criteria criteria = criteriaService.findById(evaluationRequestDto.getCriteriaId());
            EmployeeEvaluation employeeEvaluation = employeeEvaluationMapper
                    .mapToEntity(evaluationRequestDto, evaluated, questionnaire, evaluator, criteria);
            employeeEvaluation.setCreateDay(LocalDate.now());
            employeeEvaluations.add(employeeEvaluation);
        }
        return employeeEvaluationRepository.saveAll(employeeEvaluations);
    }

    /**
     * Получение оценки по id.
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeEvaluation findById(Long evaluationEvaluationId) {
        log.info("Получение оценки по идентификатору {}", evaluationEvaluationId);
        return employeeEvaluationRepository
                .findById(evaluationEvaluationId).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Оценка сотрудника с id %s не найдена",
                                evaluationEvaluationId)));
    }

    /**
     * Получение командного рейтинга за каждый месяц.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findCommandRating(String email) {
        Employee employee = employeeService.findByEmail(email);
        Long adminId = employee.getCreator().getId();
        log.info("Получение командного рейтинга идентификатору сотрудника");
        return employeeEvaluationRepository.findCommandRating(adminId);
    }

    /**
     * Получение персонального рейтинга за каждый месяц для сотрудника.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findPersonalRating(String email) {
        log.info("Получение персонального рейтинга за каждый месяц по своему email");
        return employeeEvaluationRepository.findPersonalRating(email);
    }

    /**
     * Получение командного рейтинга за каждый месяц для админа.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findCommandRatingForAdmin(String adminEmail) {
        Employee employee = employeeService.findByEmail(adminEmail);
        Long adminId = employee.getId();
        log.info("Получение командного рейтинга для админа");
        return employeeEvaluationRepository.findCommandRating(adminId);
    }

    @Override
    public List<Employee> findAllRatedByMe(String email) {
        log.info("Получение списка оцененных коллег");
        return employeeEvaluationRepository.findAllRatedByMe(email);
    }

    @Override
    public List<Employee> findAllRated(String email) {
        log.info("Получение списка всех оцененых подчиненных для админа");
        return employeeEvaluationRepository.findAllRated(email);
    }

    @Override
    public ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireId(String email, Long questionnaireId) {
        List<ResponseEmployeeEvaluationShortDto> adminEvaluations = employeeEvaluationRepository
                .findAllEvaluationsAdmin(email, questionnaireId);
        List<ResponseEmployeeEvaluationShortDto> usersEvaluations = employeeEvaluationRepository
                .findAllEvaluationsUsers(email, questionnaireId);
        ResponseRecommendationShortDto recommendation = recommendationMapper.mapToShortDto(recommendationRepository
                .findByRecipientEmailAndQuestionnaireId(email, questionnaireId));
        log.info("Получение оцененок и рекомендации по id анкеты для руководителя");
        return ResponseEmployeeEvaluationQuestionnaireDto
                .builder()
                .adminEvaluation(adminEvaluations)
                .usersEvaluation(usersEvaluations)
                .recommendation(recommendation)
                .build();
    }

    @Override
    public ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireIdForAdmin(String adminEmail,
                                                                                                  Long questionnaireId,
                                                                                                  Long evaluatedId) {
        List<ResponseEmployeeEvaluationShortDto> adminEvaluations = employeeEvaluationRepository
                .findAllEvaluationsForAdmin(adminEmail, evaluatedId, questionnaireId);
        List<ResponseEmployeeEvaluationShortDto> usersEvaluations = employeeEvaluationRepository
                .findAllEvaluationsUsersForAdmin(evaluatedId, questionnaireId);
        ResponseRecommendationShortDto recommendation = recommendationMapper.mapToShortDto(recommendationRepository
                .findByRecipientIdAndQuestionnaireId(evaluatedId, questionnaireId));
        log.info("Получение оцененок и рекомендации по id анкеты и id сотрудника для руководителя");
        return ResponseEmployeeEvaluationQuestionnaireDto
                .builder()
                .adminEvaluation(adminEvaluations)
                .usersEvaluation(usersEvaluations)
                .recommendation(recommendation)
                .build();
    }

    /**
     * Получение персонального рейтинга каждого сотрудника за каждый месяц.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponsePersonalRatingDto> findPersonalRatingAdmin(String email) {
        List<Employee> employees = employeeService.findAllByCreatorEmail(email);
        List<ResponsePersonalRatingDto> personalRatingList = new ArrayList<>(employees.size());

        for (Employee evaluation : employees) {
            ResponseEmployeeShortDto employeeShortDto = employeeMapper.mapToShortDto(evaluation);
            List<ResponseRatingFullDto> ratingForMonth = employeeEvaluationRepository
                    .findPersonalRating(evaluation.getEmail());
            ResponsePersonalRatingDto personalRating = ResponsePersonalRatingDto
                    .builder()
                    .employee(employeeShortDto)
                    .ratingByMonth(ratingForMonth)
                    .build();
            personalRatingList.add(personalRating);
        }
        log.info("Получение рейтинга сотрудников для руководителя");
        return personalRatingList;
    }
}
