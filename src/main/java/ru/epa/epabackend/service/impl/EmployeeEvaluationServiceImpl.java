package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.RequestEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseRatingDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.service.CriteriaService;
import ru.epa.epabackend.service.EmployeeEvaluationService;
import ru.epa.epabackend.service.EmployeeService;

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
    private final EmployeeService employeeService;
    private final CriteriaService criteriaService;

    /**
     * Сохранение оценки.
     */
    @Override
    public List<EmployeeEvaluation> create(String email,
                                           Long evaluatedId,
                                           List<RequestEmployeeEvaluationDto> evaluationRequestDtoList) {
        Employee evaluated = employeeService.findById(evaluatedId);
        Employee evaluator = employeeService.findByEmail(email);

        List<EmployeeEvaluation> employeeEvaluations = new ArrayList<>(evaluationRequestDtoList.size());

        for (RequestEmployeeEvaluationDto evaluationRequestDto : evaluationRequestDtoList) {
            Criteria criteria = criteriaService.findById(evaluationRequestDto.getCriteriaId());
            EmployeeEvaluation employeeEvaluation = employeeEvaluationMapper
                    .mapToEntity(evaluationRequestDto, evaluated, evaluator, criteria);
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
}
