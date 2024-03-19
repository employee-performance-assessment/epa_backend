package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Evaluation;
import ru.epa.epabackend.repository.EmployeeEvaluationRepository;
import ru.epa.epabackend.service.EmployeeEvaluationService;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.EvaluationService;

import java.time.LocalDate;
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
    private final EvaluationService evaluationService;

    /**
     * Сохранение оценки.
     */
    @Override
    public EmployeeEvaluation create(Long employeeId, Long appraiserId, EmployeeEvaluationRequestDto employeeEvaluationRequestDto) {
        Employee appraiser = employeeService.findById(appraiserId);
        Employee employee = employeeService.findById(employeeId);
        Evaluation evaluation = evaluationService.findById(employeeEvaluationRequestDto.getEvaluationId());
        LocalDate createDay = LocalDate.now();
        return employeeEvaluationRepository.save(employeeEvaluationMapper
                .mapToEntity(employeeEvaluationRequestDto, appraiser, employee, evaluation, createDay));
    }

    /**
     * Получение оценки по id.
     */
    @Override
    public EmployeeEvaluation findById(Long evaluationEvaluationId) {
        return employeeEvaluationRepository.findById(evaluationEvaluationId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Оценка сотрудника с id %s не найдена",
                        evaluationEvaluationId)));
    }

    /**
     * Получение списка оценок по id сотрудника.
     */
    @Override
    public List<EmployeeEvaluation> findAllByAppraiserId(Long appraiserId) {
        return employeeEvaluationRepository.findAllByAppraiserId(appraiserId);
    }

    /**
     * Удаление оценки.
     */
    @Override
    public void delete(Long evaluationEvaluationId) {
        if (employeeEvaluationRepository.existsById(evaluationEvaluationId)) {
            employeeEvaluationRepository.deleteById(evaluationEvaluationId);
        } else {
            throw new EntityNotFoundException(String.format("Оценка с id %s не найден", evaluationEvaluationId));
        }
    }
}
