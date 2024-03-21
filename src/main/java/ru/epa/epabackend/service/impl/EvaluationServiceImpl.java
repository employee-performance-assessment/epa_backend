package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.EvaluationRequestDto;
import ru.epa.epabackend.mapper.EvaluationMapper;
import ru.epa.epabackend.model.Evaluation;
import ru.epa.epabackend.repository.EvaluationRepository;
import ru.epa.epabackend.service.EvaluationService;

import java.util.List;

/**
 * Класс EvaluationServiceImpl содержит бизнес-логику работы с критериями оценок.
 *
 * @author Михаил Безуглов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationMapper evaluationMapper;

    /**
     * Сохранение нового критерия оценки.
     */
    @Override
    public Evaluation create(EvaluationRequestDto evaluationRequestDto) {
        return evaluationRepository.save(evaluationMapper.mapToEntity(evaluationRequestDto));
    }

    /**
     * Получение критерия оценки по её ID.
     */
    @Override
    public Evaluation findById(Long evaluationId) {
        return evaluationRepository.findById(evaluationId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Оценка с id %s не найдена", evaluationId)));
    }

    /**
     * Получение списка критериев оценок.
     */
    @Override
    public List<Evaluation> findAll() {
        return evaluationRepository.findAll();
    }

    /**
     * Удаление критерия оценки по её ID.
     */
    @Override
    public void delete(Long evaluationId) {
        if (evaluationRepository.existsById(evaluationId)) {
            evaluationRepository.deleteById(evaluationId);
        } else {
            throw new EntityNotFoundException(String.format("Оценка с id %s не найден", evaluationId));
        }
    }
}
