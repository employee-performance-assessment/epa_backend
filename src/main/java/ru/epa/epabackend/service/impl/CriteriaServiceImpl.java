package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.CriteriaRequestDto;
import ru.epa.epabackend.mapper.CriteriaMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.repository.CriteriaRepository;
import ru.epa.epabackend.service.CriteriaService;

import java.util.List;

/**
 * Класс EvaluationServiceImpl содержит бизнес-логику работы с критериями оценок.
 *
 * @author Михаил Безуглов
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CriteriaServiceImpl implements CriteriaService {

    private final CriteriaRepository criteriaRepository;
    private final CriteriaMapper criteriaMapper;

    /**
     * Сохранение нового критерия оценки.
     */
    @Override
    public Criteria create(CriteriaRequestDto criteriaRequestDto) {
        log.info("Сохранение нового критерия оценки {}", criteriaRequestDto.getName());
        Criteria criteria = criteriaRepository.save(criteriaMapper.mapToEntity(criteriaRequestDto));
        return criteria;
    }

    /**
     * Получение критерия оценки по её ID.
     */
    @Override
    public Criteria findById(Long criteriaId) {
        log.info("Получение оценки по идентификатору {}", criteriaId);
        Criteria criteria = criteriaRepository.findById(criteriaId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Оценка с id %s не найдена", criteriaId)));
        return criteria;
    }

    /**
     * Получение списка критериев оценок.
     */
    @Override
    public List<Criteria> findAll() {
        log.info("Получение списка критериев оценок");
        return criteriaRepository.findAll();
    }

    /**
     * Удаление критерия оценки по её ID.
     */
    @Override
    public void delete(Long criteriaId) {
        log.info("Удаление критерия оценки по её идентификатору {}", criteriaId);
        if (criteriaRepository.existsById(criteriaId)) {
            criteriaRepository.deleteById(criteriaId);
        } else {
            throw new EntityNotFoundException(String.format("Оценка с id %s не найден", criteriaId));
        }
    }
}
