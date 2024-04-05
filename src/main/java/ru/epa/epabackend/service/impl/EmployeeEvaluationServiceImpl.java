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
import ru.epa.epabackend.mapper.QuestionnaireMapper;
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
import java.util.HashMap;
import java.util.List;

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
    private final QuestionnaireMapper questionnaireMapper;
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
        Long adminId = employee.getCreator() == null
                ? employee.getId()
                : employee.getCreator().getId();
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
     * Получение списка оцененных коллег.
     */

    @Override
    public List<Employee> findAllRatedByMe(String email) {
        log.info("Получение списка оцененных коллег");
        return employeeEvaluationRepository.findAllRatedByMe(email);
    }

    /**
     * Получение списка подчиненных для админа.
     */
    @Override
    public List<Employee> findAllRated(String email) {
        log.info("Получение списка всех оцененных подчиненных для админа");
        return employeeEvaluationRepository.findAllRated(email);
    }

    /**
     * Получение оценок по id анкеты.
     */
    @Override
    public ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireId(String email, Long questionnaireId) {
        List<ResponseEmployeeEvaluationShortDto> adminEvaluations = employeeEvaluationRepository
                .findAllEvaluationsAdmin(email, questionnaireId);
        List<ResponseEmployeeEvaluationShortDto> usersEvaluations = employeeEvaluationRepository
                .findAllEvaluationsUsers(email, questionnaireId);
        ResponseRecommendationShortDto recommendation = recommendationMapper.mapToShortDto(recommendationRepository
                .findByRecipientEmailAndQuestionnaireId(email, questionnaireId));
        log.info("Получение оценок и рекомендации по id анкеты для руководителя");
        return ResponseEmployeeEvaluationQuestionnaireDto
                .builder()
                .evaluations(filterEvaluations(adminEvaluations, usersEvaluations))
                .recommendation(recommendation)
                .build();
    }

    /**
     * Получение оценок по id анкеты и id сотрудника.
     */
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
        log.info("Получение оценок и рекомендации по id анкеты и id сотрудника для руководителя");
        return ResponseEmployeeEvaluationQuestionnaireDto
                .builder()
                .evaluations(filterEvaluations(adminEvaluations, usersEvaluations))
                .recommendation(recommendation)
                .build();
    }

    /**
     * Получение поставленных оценок по id анкеты и id сотрудника.
     */
    @Override
    public List<ResponseMyEvaluationsDto> findAllMyEvaluationsByEvaluatedId(String email, Long evaluatedId) {
        List<EmployeeEvaluation> evaluations = employeeEvaluationRepository
                .findAllByEvaluatorEmailAndEvaluatedId(email, evaluatedId);
        return filterMyEvaluations(evaluations);
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

    /**
     * Сортировка оценок по оцененным коллегам и анкетам.
     */
    private List<ResponseMyEvaluationsDto> filterMyEvaluations(List<EmployeeEvaluation> evaluations) {
        List<ResponseMyEvaluationsDto> myEmployeeEvaluations = new ArrayList<>(evaluations.size());
        for (EmployeeEvaluation evaluation : evaluations) {
            ResponseMyEvaluationsDto responseMyEmployeeEvaluationsDto = ResponseMyEvaluationsDto
                    .builder()
                    .questionnaire(questionnaireMapper.mapToShortResponseDto(evaluation.getQuestionnaire()))
                    .responseEmployeeEvaluationList(employeeEvaluationMapper.mapDtoList(List.of(evaluation)))
                    .build();

            if (!myEmployeeEvaluations.contains(responseMyEmployeeEvaluationsDto)) {
                myEmployeeEvaluations.add(responseMyEmployeeEvaluationsDto);
            } else {
                int index = myEmployeeEvaluations.indexOf(responseMyEmployeeEvaluationsDto);
                List<ResponseEmployeeEvaluationShortDto> myEvaluationsAdd = myEmployeeEvaluations.get(index)
                        .getResponseEmployeeEvaluationList();
                myEvaluationsAdd.add(employeeEvaluationMapper.mapToShortDto(evaluation));
                myEmployeeEvaluations.get(index).setResponseEmployeeEvaluationList(myEvaluationsAdd);
            }
        }
        return myEmployeeEvaluations;
    }

    private HashMap<String, ResponseEvaluationsAdminUserDto> filterEvaluations(
            List<ResponseEmployeeEvaluationShortDto> adminEvaluations,
            List<ResponseEmployeeEvaluationShortDto> usersEvaluations) {

        HashMap<String, ResponseEvaluationsAdminUserDto> evaluations = new HashMap<>(adminEvaluations.size());

        for (int i = 0; i < adminEvaluations.size(); i++) {
            ResponseEvaluationsAdminUserDto evaluationsForCriteria = ResponseEvaluationsAdminUserDto
                    .builder()
                    .adminScore(adminEvaluations.get(i).getScore())
                    .colleaguesScore(usersEvaluations.get(i).getScore())
                    .build();
            evaluations.put(adminEvaluations.get(i).getName(), evaluationsForCriteria);
        }
        return evaluations;
    }
}
