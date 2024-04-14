package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.exception.exceptions.BadRequestException;
import ru.epa.epabackend.exception.exceptions.ConflictException;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.*;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.repository.RecommendationRepository;
import ru.epa.epabackend.service.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.epa.epabackend.util.Constants.THIRTY_DAYS;

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
    private final EmployeeService employeeService;
    private final CriteriaService criteriaService;
    private final QuestionnaireService questionnaireService;
    private final RecommendationService recommendationService;

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
        employeeService.checkEvaluatorForEmployee(evaluator, evaluated);
        checkQuestionnaireForEvaluator(questionnaire, evaluator);
        List<Long> criteriaIds = evaluationRequestDtoList.stream()
                .map(RequestEmployeeEvaluationDto::getCriteriaId).collect(Collectors.toList());
        if(employeeEvaluationRepository.existsByCriteriaIdIn(criteriaIds)) {
            throw new ConflictException(String.format("Существует оценка по одному или нескольким критериям анкеты " +
                    "для оцениваемого пользователя %s", evaluated.getFullName()));
        }
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
     * Получение командного рейтинга за каждый месяц указанного года.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findCommandRating(String email, Integer year) {
        Employee employee = employeeService.findByEmail(email);
        Long adminId = employee.getCreator() == null
                ? employee.getId()
                : employee.getCreator().getId();
        log.info("Получение командного рейтинга по идентификатору руководителя {} и году {}", adminId, year);
        return employeeEvaluationRepository.findCommandRating(adminId, year);
    }

    /**
     * Получение персонального рейтинга за каждый месяц для сотрудника.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findPersonalRating(String email, Integer year) {
        log.info("Получение персонального рейтинга за каждый месяц по своему email");
        return employeeEvaluationRepository.findPersonalRating(email, year);
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
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        Employee employee = employeeService.findByEmail(email);
        checkQuestionnaireForEvaluator(questionnaire, employee);
        Recommendation recommendation = recommendationRepository
                .findByRecipientEmailAndQuestionnaireId(email, questionnaireId);
        ResponseRatingDto responseRatingDto = employeeEvaluationRepository
                .findRatingByQuestionnaireIdAndEvaluatedEmail(questionnaireId, email);
        log.info("Получение своих оценок и рекомендации по id анкеты");
        return ResponseEmployeeEvaluationQuestionnaireDto
                .builder()
                .createQuestionnaire(questionnaire.getCreated())
                .middleScore(responseRatingDto == null ? 0 : responseRatingDto.getRating())
                .evaluations(filterEvaluations(adminEvaluations, usersEvaluations, questionnaire.getCriterias()))
                .recommendation(recommendation == null
                        ? "" : recommendation.getRecommendation())
                .build();
    }

    /**
     * Получение оценок по email оценивающего, id анкеты и id сотрудника.
     */
    @Override
    public ResponseEmployeeEvaluationQuestionnaireDto findAllEvaluationsByQuestionnaireIdForAdmin(String adminEmail,
                                                                                                  Long questionnaireId,
                                                                                                  Long evaluatedId) {
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        Employee admin = employeeService.findByEmail(adminEmail);
        Employee employee = employeeService.findById(evaluatedId);
        employeeService.checkAdminForEmployee(admin, employee);
        questionnaireService.checkAdminForQuestionnaire(admin, questionnaire);
        List<ResponseEmployeeEvaluationShortDto> adminEvaluations = employeeEvaluationRepository
                .findAllEvaluationsForAdmin(adminEmail, evaluatedId, questionnaireId);
        List<ResponseEmployeeEvaluationShortDto> usersEvaluations = employeeEvaluationRepository
                .findAllEvaluationsUsersForAdmin(evaluatedId, questionnaireId);
        Recommendation recommendation = recommendationRepository
                .getByRecipientIdAndQuestionnaireId(evaluatedId, questionnaireId);
        ResponseRatingDto responseRatingDto = employeeEvaluationRepository
                .findRatingByQuestionnaireIdAndEvaluatedId(questionnaireId, evaluatedId);
        log.info("Получение оценок и рекомендации по id анкеты и id сотрудника для руководителя");
        return ResponseEmployeeEvaluationQuestionnaireDto
                .builder()
                .createQuestionnaire(questionnaire.getCreated())
                .middleScore(responseRatingDto == null
                        ? 0 : responseRatingDto.getRating())
                .evaluations(filterEvaluations(adminEvaluations, usersEvaluations, questionnaire.getCriterias()))
                .recommendation(recommendation == null
                        ? "" : recommendation.getRecommendation())
                .build();
    }

    @Override
    public List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesAssessed(String email, String text,
                                                                               LocalDate from, LocalDate to) {
        Employee employee = employeeService.findByEmail(email);
        return employeeEvaluationRepository.findEmployeesQuestionnairesAssessed(employee.getId(), text, from, to);
    }

    @Override
    public List<ResponseEmployeeEvaluationShortDto> findQuestionnaireScores(String email, Long questionnaireId,
                                                                            Long evaluatedId) {
        Employee evaluator = employeeService.findByEmail(email);
        Employee evaluated = employeeService.findById(evaluatedId);
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        employeeService.checkEvaluatorForEmployee(evaluator, evaluated);
        questionnaireService.checkAdminForQuestionnaire(evaluator.getCreator() == null ? evaluator : evaluator.getCreator(),
                questionnaire);
        List<EmployeeEvaluation> employeeEvaluations = employeeEvaluationRepository
                .findByEvaluatorIdAndEvaluatedIdAndQuestionnaireId(evaluator.getId(), evaluatedId, questionnaireId);
        return employeeEvaluationMapper.mapToShortListDto(employeeEvaluations);
    }

    @Override
    public ResponseAdminEvaluationDto findAssessedQuestionnaireByAdmin(String email, Long questionnaireId,
                                                                       Long evaluatedId) {
        Employee admin = employeeService.findByEmail(email);
        Employee employee = employeeService.findById(evaluatedId);
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        employeeService.checkAdminForEmployee(admin, employee);
        questionnaireService.checkAdminForQuestionnaire(admin, questionnaire);
        List<ResponseEmployeeEvaluationShortDto> adminEvaluations = findQuestionnaireScores(email, questionnaireId,
                evaluatedId);
        String stringRecommendation = recommendationService
                .getByRecipientIdAndQuestionnaireId(evaluatedId, questionnaireId).getRecommendation();
        return ResponseAdminEvaluationDto.builder()
                .adminEvaluations(adminEvaluations)
                .recommendation(stringRecommendation)
                .build();
    }

    /**
     * Получение анкет в которых оценен сотрудник с ID.
     */
    @Override
    public List<ResponseEvaluatedQuestionnaireDto> findAllQuestionnaireByEvaluatedId(
            String adminEmail, Long evaluatedId, Integer stars, LocalDate from, LocalDate to) {
        return employeeEvaluationRepository.findListQuestionnaireByAdminEmailAndEvaluatedId(adminEmail, evaluatedId, stars, from, to);
    }

    /**
     * Получение списка анкет в которых оценен сотрудник с email.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseEvaluatedQuestionnaireDto> findAllQuestionnaireByEvaluatedEmail(
            String email, Integer stars, LocalDate from, LocalDate to) {
        Employee employee = employeeService.findByEmail(email);
        return employeeEvaluationRepository.findListQuestionnaireByEvaluatedId(employee.getId(), stars, from, to);
    }

    @Override
    public List<ResponseEmployeeAssessDto> findEmployeesQuestionnairesForAssessment(String email, String text,
                                                                                    LocalDate from, LocalDate to) {
        Employee employee = employeeService.findByEmail(email);
        LocalDate startDate = LocalDate.now().minusDays(THIRTY_DAYS);
        if (employee.getCreator() == null) {
            return employeeEvaluationRepository.findEmployeesQuestionnairesForAssessmentByAdmin(employee.getId(),
                    startDate, text, from, to);
        } else {
            LocalDate employeeCreated = employee.getCreated();
            return employeeEvaluationRepository.findEmployeesQuestionnairesForAssessment(employee.getId(),
                    startDate, text, from, to, employeeCreated);
        }
    }

    /**
     * Получение руководителем персонального рейтинга сотрудника за каждый месяц указанного года.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findPersonalRatingAdmin(String email, Long evaluatedId, Integer year) {
        log.info("Получение руководителем персонального рейтинга сотрудника с id {} за каждый месяц {} года.",
                evaluatedId, year);
        Employee admin = employeeService.findByEmail(email);
        Employee employee = employeeService.findById(evaluatedId);
        employeeService.checkAdminForEmployee(admin, employee);
        return employeeEvaluationRepository.findPersonalRatingByAdmin(evaluatedId, year);
    }

    /**
     * Получение сотрудником своего среднего рейтинга за текущий месяц.
     */
    @Override
    @Transactional(readOnly = true)
    public Double findAverageRatingByUser(Principal principal, LocalDate rangeStart, LocalDate rangeEnd) {
        log.info("Получение сотрудником своего среднего рейтинга за текущий месяц");
        Employee employee = employeeService.findByEmail(principal.getName());
        return employeeEvaluationRepository.getAverageRatingByEvaluatedIdAndCurrentMonth(employee.getId(),
                rangeStart, rangeEnd);
    }

    /**
     * Получение администратором среднего рейтинга сотрудника за текущий месяц.
     */
    @Override
    @Transactional(readOnly = true)
    public Double findAverageRatingByAdmin(String email, Long employeeId, LocalDate rangeStart, LocalDate rangeEnd) {
        log.info("Получение администратором среднего рейтинга сотрудника за текущий месяц");
        Employee employee = employeeService.findById(employeeId);
        Employee admin = employeeService.findByEmail(email);
        employeeService.checkAdminForEmployee(admin, employee);
        return employeeEvaluationRepository.getAverageRatingByEvaluatedIdAndCurrentMonth(employeeId, rangeStart, rangeEnd);
    }

    private HashMap<String, ResponseEvaluationsAdminUserDto> filterEvaluations(
            List<ResponseEmployeeEvaluationShortDto> adminEvaluations,
            List<ResponseEmployeeEvaluationShortDto> usersEvaluations, List<Criteria> criteria) {

        HashMap<String, ResponseEvaluationsAdminUserDto> evaluations = new HashMap<>(criteria.size());

        for (int i = 0; i < criteria.size(); i++) {
            double adminScore = adminEvaluations.isEmpty() ? 0 : adminEvaluations.get(i).getScore();
            double userScore = usersEvaluations.isEmpty() ? 0 : usersEvaluations.get(i).getScore();
            ResponseEvaluationsAdminUserDto evaluationsForCriteria = ResponseEvaluationsAdminUserDto
                    .builder()
                    .adminScore(adminScore)
                    .colleaguesScore(userScore)
                    .build();
            evaluations.put(criteria.get(i).getName(), evaluationsForCriteria);
        }
        return evaluations;
    }

    /**
     * Проверка, что сотрудник оценивает своего коллегу по анкете своего руководителя
     */
    @Override
    @Transactional(readOnly = true)
    public void checkQuestionnaireForEvaluator(Questionnaire questionnaire, Employee evaluator) {
        if (evaluator.getCreator() != null
                && !Objects.equals(questionnaire.getAuthor().getId(), evaluator.getCreator().getId())) {
            throw new BadRequestException("Анкета создана не вашим руководителем");
        } else if (evaluator.getCreator() == null
                && !Objects.equals(questionnaire.getAuthor().getId(), evaluator.getId())) {
            throw new BadRequestException("Анкета создана не вами");
        }
    }
}
