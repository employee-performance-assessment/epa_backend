package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.ResponseEmployeeShortDto;
import ru.epa.epabackend.dto.evaluation.*;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.mapper.QuestionnaireMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.service.CriteriaService;
import ru.epa.epabackend.service.EmployeeEvaluationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.QuestionnaireService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс EmployeeEvaluationServiceImpl содержит бизнес-логику работы с оценками сотрудников своих коллег.
 *
 * @author Михаил Безуглов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeEvaluationServiceImpl implements EmployeeEvaluationService {

    private final EmployeeEvaluationRepository employeeEvaluationRepository;
    private final EmployeeEvaluationMapper employeeEvaluationMapper;
    private final EmployeeMapper employeeMapper;
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
        return employeeEvaluationRepository
                .findById(evaluationEvaluationId).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Оценка сотрудника с id %s не найдена",
                                evaluationEvaluationId)));
    }

    /**
     * Получение списка своих оценок от коллег по своему email.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseEmployeeEvaluationDto> findAllEvaluationsUsers(String email) {
        return employeeEvaluationRepository.findAllEvaluationsUsers(email);
    }

    /**
     * Получение списка своих оценок от руководителя по своему email.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseEmployeeEvaluationDto> findAllEvaluationsAdmin(String email) {
        return employeeEvaluationRepository.findAllEvaluationsAdmin(email);
    }

    /**
     * Получение рейтинга сотрудника от всего коллектива.
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseRatingDto findFullRating(String email, LocalDate startDay, LocalDate endDay) {
        return employeeEvaluationRepository.findFullRating(email, startDay, endDay);
    }

    /**
     * Получение рейтинга сотрудника только от руководителя.
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseRatingDto findRatingByAdmin(String email, LocalDate startDay, LocalDate endDay) {
        return employeeEvaluationRepository.findRatingByAdmin(email, startDay, endDay);
    }

    /**
     * Получение списка оцененных коллег по ID анкеты.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseMyEvaluationsDto> findAllMyEvaluationsById(String email, Long questionnaireId) {
        List<EmployeeEvaluation> evaluations = employeeEvaluationRepository
                .findAllByEvaluatorEmailAndQuestionnaireId(email,questionnaireId);
        return sortMyEvaluations(evaluations);
    }

    /**
     * Получение списка оцененных коллег по всем анкетам.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseMyEvaluationsDto> findAllMyEvaluations(String email) {
        List<EmployeeEvaluation> evaluations = employeeEvaluationRepository.findAllByEvaluatorEmail(email);
        return sortMyEvaluations(evaluations);
    }

    /**
     * Получение командного рейтинга за каждый месяц.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findCommandRating(String email) {
        Employee employee = employeeService.findByEmail(email);
        assert employee.getCreator() != null;
        Long adminId = employee.getCreator().getId();
        return employeeEvaluationRepository.findCommandRating(adminId);
    }

    /**
     * Получение персонального рейтинга за каждый месяц для сотрудника.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResponseRatingFullDto> findPersonalRating(String email) {
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
        return employeeEvaluationRepository.findCommandRating(adminId);
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
        return personalRatingList;
    }

    /**
     * Сортировка оценок по оцененным коллегам и анкетам.
     */
    private List<ResponseMyEvaluationsDto> sortMyEvaluations(List<EmployeeEvaluation> evaluations) {
        List<ResponseMyEvaluationsDto> myEmployeeEvaluations = new ArrayList<>(evaluations.size());
        for (EmployeeEvaluation evaluation : evaluations) {
            ResponseMyEvaluationsDto responseMyEmployeeEvaluationsDto = ResponseMyEvaluationsDto
                    .builder()
                    .evaluated(employeeMapper.mapToShortDto(evaluation.getEvaluated()))
                    .questionnaire(questionnaireMapper.mapToShortResponseDto(evaluation.getQuestionnaire()))
                    .responseEmployeeEvaluationList(employeeEvaluationMapper.mapDtoList(List.of(evaluation)))
                    .build();

            if (!myEmployeeEvaluations.contains(responseMyEmployeeEvaluationsDto)) {
                myEmployeeEvaluations.add(responseMyEmployeeEvaluationsDto);
            } else {
                int index = myEmployeeEvaluations.indexOf(responseMyEmployeeEvaluationsDto);
                List<ResponseEmployeeEvaluationDto> myEvaluationsAdd = myEmployeeEvaluations.get(index)
                        .getResponseEmployeeEvaluationList();
                myEvaluationsAdd.add(employeeEvaluationMapper.mapToShortDto(evaluation));
                myEmployeeEvaluations.get(index).setResponseEmployeeEvaluationList(myEvaluationsAdd);
            }
        }
        return myEmployeeEvaluations;
    }
}
