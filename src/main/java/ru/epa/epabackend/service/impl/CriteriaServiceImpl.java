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
import java.util.Set;
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
                new EntityNotFoundException(String.format("Критерий оценивания с id %s не найдена", criteriaId)));
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
            throw new EntityNotFoundException(String.format("Критерий оценивания с id %s не найден", criteriaId));
        }
    }

    @Override
    public List<Criteria> findDefault() {
        return criteriaRepository.findAllByIdBetweenOrderByIdAsc(1L, 11L);
    }

    @Override
    public boolean isNameExists(String name) {
        return criteriaRepository.existsByName(name);
    }

    @Override
    public Criteria findByName(String name) {
        return criteriaRepository.findByName(name).orElseThrow(() ->
                new EntityNotFoundException(String.format("Критерий с именем %s не найден", name)));
    }

    @Override
    public Set<Criteria> findExistentAndSaveNonExistentCriterias(Set<CriteriaRequestDto> criterias) {
        return criterias.stream().map(c -> {
            if (isNameExists(c.getName())) return findByName(c.getName());
            else return create(c);
        }).collect(Collectors.toSet());
    }
}
