package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.criteria.CriteriaRequestDto;
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
    public List<Criteria> create(List<CriteriaRequestDto> criteriaRequestDtoList) {
        log.info("Сохранение списка критериев оценок");
        return criteriaRepository.saveAll(criteriaMapper.mapListToEntity(criteriaRequestDtoList));
    }

    /**
     * Получение критерия оценки по её ID.
     */
    @Override
    @Transactional(readOnly = true)
    public Criteria findById(Long criteriaId) {
        log.info("Получение оценки по идентификатору {}", criteriaId);
        Criteria criteria = criteriaRepository.findById(criteriaId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Критерий оценки с id %s не найден", criteriaId)));
        return criteria;
    }

    /**
     * Получение списка критериев оценок.
     */
    @Override
    @Transactional(readOnly = true)
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
            throw new EntityNotFoundException(String.format("Критерий оценки с id %s не найден", criteriaId));
        }
    }

    /**
     * Получение дефолтных критериев (по умолчанию)
     */
    @Override
    public List<Criteria> findDefault() {
        log.info("Получение дефолтных критериев (по умолчанию)");
        return criteriaRepository.findAllByIdBetweenOrderByIdAsc(1L, 11L);
    }

    /**
     * Проверка, существует ли в БД критерий с указанным именем
     */
    @Override
    public boolean isNameExists(String name) {
        log.info("Существует ли в БД критерий с указанным именем {}", name);
        return criteriaRepository.existsByName(name);
    }

    /**
     * Получение критерия по его имени
     */
    @Override
    public Criteria findByName(String name) {
        log.info("Получение критерия по его имени {}", name);
        return criteriaRepository.findByName(name).orElseThrow(() ->
                new EntityNotFoundException(String.format("Критерий с именем %s не найден", name)));
    }

    /**
     * Сохранение множества критериев, при котором критерии с существующими именами не перезаписываются
     */
    @Override
    public List<Criteria> findExistentAndSaveNonExistentCriterias(List<CriteriaRequestDto> criterias) {
        log.info("Сохранение множества критериев");
        return criterias.stream()
                .map(c -> criteriaRepository.findByName(c.getName())
                        .orElseGet(() -> criteriaRepository.save(criteriaMapper.mapToEntity(c)))).collect(Collectors.toList());
    }
}
