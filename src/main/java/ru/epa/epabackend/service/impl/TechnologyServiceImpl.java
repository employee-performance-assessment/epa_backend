package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.technology.RequestTechnologyDto;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.repository.TechnologyRepository;
import ru.epa.epabackend.service.TechnologyService;

import java.util.List;

/**
 * Класс TechnologyServiceImpl содержит методы действий с технологией.
 *
 * @author Артем Масалкин
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TechnologyServiceImpl implements TechnologyService {
    private final TechnologyRepository technologyRepository;
    private final TechnologyMapper technologyMapper;

    /**
     * Добавление технологии.
     */
    @Override
    public Technology create(RequestTechnologyDto technologyDto) {
        return technologyRepository.save(technologyMapper.mapToEntity(technologyDto));
    }

    /**
     * Получение технологии по идентификатору.
     */
    @Override
    @Transactional(readOnly = true)
    public Technology findById(Long technologyId) {
        return technologyRepository.findById(technologyId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Технология с id %s не найдена", technologyId)));
    }

    /**
     * Обновление технологии.
     */
    @Override
    public Technology update(RequestTechnologyDto technologyDto, Long technologyId) {
        Technology oldTechnology = findById(technologyId);
        technologyMapper.updateFields(technologyDto, oldTechnology);
        return technologyRepository.save(oldTechnology);
    }

    /**
     * Получение списка всех технологий.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Technology> findAll() {
        return technologyRepository.findAll();
    }

    /**
     * Удаление технологии по идентификатору.
     */
    @Override
    public void delete(Long technologyId) {
        if (technologyRepository.existsById(technologyId)) {
            technologyRepository.deleteById(technologyId);
        } else {
            throw new EntityNotFoundException(String.format("Технология с id %s не найдена", technologyId));
        }
    }
}