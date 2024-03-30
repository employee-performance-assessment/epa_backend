package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.service.EmployeeEvaluationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.CriteriaService;

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
    private final EmployeeEvaluationMapper employeeEvaluationMapper;
    private final EmployeeService employeeService;
    private final CriteriaService criteriaService;

    /**
     * Сохранение оценки.
     */
    @Override
    public EmployeeEvaluation create(Long evaluatorId, Long evaluatedId,
                                     EmployeeEvaluationRequestDto employeeEvaluationRequestDto) {
        log.info("Сохранение оценки");
        Employee evaluated = employeeService.findById(evaluatedId);
        Employee evaluator = employeeService.findById(evaluatorId);
        Criteria criteria = criteriaService.findById(employeeEvaluationRequestDto.getCriteriaId());
        EmployeeEvaluation employeeEvaluation = employeeEvaluationMapper
                .mapToEntity(employeeEvaluationRequestDto, evaluated, evaluator, criteria);
        return employeeEvaluationRepository.save(employeeEvaluation);
    }

    /**
     * Получение оценки по id.
     */
    @Override
    public EmployeeEvaluation findById(Long evaluationEvaluationId) {
        log.info("Получение оценки по идентификатору {}", evaluationEvaluationId);
        return employeeEvaluationRepository
                .findById(evaluationEvaluationId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Оценка сотрудника с id %s не найдена",
                        evaluationEvaluationId)));
    }

    /**
     * Получение списка оценок по id сотрудника.
     */
    @Override
    public List<EmployeeEvaluation> findAllByAppraiserId(Long evaluatedId) {
        log.info("Получение списка оценок по идентификатору сотрудника {}", evaluatedId);
        return employeeEvaluationRepository.findAllByEvaluatedId(evaluatedId);
    }

    /**
     * Удаление оценки.
     */
    @Override
    public void delete(Long evaluationEvaluationId) {
        log.info("Удаление оценки по идентификатору {}", evaluationEvaluationId);
        if (employeeEvaluationRepository.existsById(evaluationEvaluationId)) {
            employeeEvaluationRepository.deleteById(evaluationEvaluationId);
        } else {
            throw new EntityNotFoundException(String.format("Оценка с id %s не найден", evaluationEvaluationId));
        }
    }
}
