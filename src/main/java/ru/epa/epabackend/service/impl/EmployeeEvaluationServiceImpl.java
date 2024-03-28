package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationResponseDto;
import ru.epa.epabackend.dto.evaluation.RatingResponseDto;
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
    public List<EmployeeEvaluation> create(Long evaluatorId, Long evaluatedId,
                                           List<EmployeeEvaluationRequestDto> evaluationRequestDtoList) {
        Employee evaluated = employeeService.findById(evaluatedId);
        Employee evaluator = employeeService.findById(evaluatorId);

        List<EmployeeEvaluation> employeeEvaluations = new ArrayList<>();

        for (EmployeeEvaluationRequestDto evaluationRequestDto : evaluationRequestDtoList) {
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
    public EmployeeEvaluation findById(Long evaluationEvaluationId) {
        return employeeEvaluationRepository
                .findById(evaluationEvaluationId).orElseThrow(() ->
                        new EntityNotFoundException(String.format("Оценка сотрудника с id %s не найдена",
                                evaluationEvaluationId)));
    }

    /**
     * Получение списка своих оценок от коллег по своему id.
     */
    @Override
    public List<EmployeeEvaluationResponseDto> findAllEvaluationsUsers(Long evaluatedId) {
        return employeeEvaluationRepository.findAllEvaluationsUsers(evaluatedId);
    }

    /**
     * Получение списка своих оценок от руководителя по своему id.
     */
    @Override
    public List<EmployeeEvaluationResponseDto> findAllEvaluationsAdmin(Long evaluatedId) {
        return employeeEvaluationRepository.findAllEvaluationsAdmin(evaluatedId);
    }

    /**
     * Получение рейтинга сотрудника от всего коллектива.
     */
    @Override
    public RatingResponseDto findFullRating(Long evaluatedId, LocalDate startDay, LocalDate endDay) {
        return employeeEvaluationRepository.findFullRating(evaluatedId, startDay, endDay);
    }

    /**
     * Получение рейтинга сотрудника только от руководителя.
     */
    @Override
    public RatingResponseDto findRatingByAdmin(Long evaluatedId, LocalDate startDay, LocalDate endDay) {
        return employeeEvaluationRepository.findRatingByAdmin(evaluatedId, startDay, endDay);
    }
}
