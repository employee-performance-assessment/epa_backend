package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;
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
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CriteriaServiceImpl implements CriteriaService {

    private final CriteriaRepository criteriaRepository;
    private final CriteriaMapper criteriaMapper;

    /**
     * Сохранение списка критериев оценок.
     */
    @Override
    public List<Criteria> create(List<RequestCriteriaDto> requestCriteriaDtoList) {
        log.info("Сохранение списка критериев оценок");
        return criteriaRepository.saveAll(criteriaMapper.mapListToEntity(requestCriteriaDtoList));
    }

    /**
     * Получение критерия оценки по её ID.
     */
    @Override
    @Transactional(readOnly = true)
    public Criteria findById(Long criteriaId) {
        log.info("Получение оценки по идентификатору {}", criteriaId);
        return criteriaRepository.findById(criteriaId).orElseThrow(() ->
                new EntityNotFoundException("Критерий оценки не найден"));
    }

    /**
     * Получение дефолтных критериев (по умолчанию)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Criteria> findDefault() {
        log.info("Получение дефолтных критериев (по умолчанию)");
        return criteriaRepository.findAllByIsDefault(true);
    }

    /**
     * Сохранение множества критериев, при котором критерии с существующими именами не перезаписываются
     */
    @Override
    public List<Criteria> findExistentAndSaveNonExistentCriterias(List<RequestCriteriaDto> criterias) {
        log.info("Сохранение множества критериев");
        return criterias.stream()
                .map(c -> criteriaRepository.findByName(c.getName())
                        .orElseGet(() -> criteriaRepository.save(criteriaMapper.mapToEntity(c)))).collect(Collectors.toList());
    }
}
