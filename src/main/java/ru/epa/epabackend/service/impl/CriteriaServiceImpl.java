package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.evaluation.CriteriaRequestDto;
import ru.epa.epabackend.mapper.CriteriaMapper;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.repository.CriteriaRepository;
import ru.epa.epabackend.service.CriteriaService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс EvaluationServiceImpl содержит бизнес-логику работы с критериями оценок.
 *
 * @author Михаил Безуглов
 */
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
        Criteria criteria = criteriaRepository.save(criteriaMapper.mapToEntity(criteriaRequestDto));
        return criteria;
    }

    /**
     * Получение критерия оценки по её ID.
     */
    @Override
    public Criteria findById(Long criteriaId) {
        Criteria criteria = criteriaRepository.findById(criteriaId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Критерий оценки с id %s не найден", criteriaId)));
        return criteria;
    }

    /**
     * Получение списка критериев оценок.
     */
    @Override
    public List<Criteria> findAll() {
        return criteriaRepository.findAll();
    }

    /**
     * Удаление критерия оценки по её ID.
     */
    @Override
    public void delete(Long criteriaId) {
        if (criteriaRepository.existsById(criteriaId)) {
            criteriaRepository.deleteById(criteriaId);
        } else {
            throw new EntityNotFoundException(String.format("Критерий оценки с id %s не найден", criteriaId));
        }
    }

    /**
     * Получение дефолтных критериев (по умолчанию)
     */
    @Override
    public List<Criteria> findDefault() {
        return criteriaRepository.findAllByIdBetweenOrderByIdAsc(1L, 11L);
    }

    /**
     * Сохранение множества критериев, при котором критерии с существующими именами не перезаписываются
     */
    @Override
    public List<Criteria> findExistentAndSaveNonExistentCriterias(List<CriteriaRequestDto> criterias) {
        return criterias.stream()
                .map(c -> criteriaRepository.findByName(c.getName())
                        .orElse(create(c)))
                .collect(Collectors.toList());
    }
}
