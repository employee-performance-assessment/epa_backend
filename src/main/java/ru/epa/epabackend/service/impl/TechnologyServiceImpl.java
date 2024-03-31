package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {
    private final TechnologyRepository technologyRepository;
    private final TechnologyMapper technologyMapper;

    /**
     * Добавление технологии.
     */
    @Transactional
    public Technology create(TechnologyRequestDto technologyDto) {
        log.info("Добавление технологии {}", technologyDto.getName());
        return technologyRepository.save(technologyMapper.mapToEntity(technologyDto));
    }

    /**
     * Получение технологии по идентификатору.
     */
    @Transactional
    public Technology findById(Long technologyId) {
        log.info("Получение технологии по идентификатору {}", technologyId);
        return technologyRepository.findById(technologyId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Технология с id %s не найдена", technologyId)));
    }

    /**
     * Обновление технологии.
     */
    @Transactional
    public Technology update(TechnologyRequestDto technologyDto, Long technologyId) {
        log.info("Получение технологии {} по идентификатору {}", technologyDto.getName(), technologyId);
        Technology oldTechnology = findById(technologyId);
        technologyMapper.updateFields(technologyDto, oldTechnology);
        return technologyRepository.save(oldTechnology);
    }

    /**
     * Получение списка всех технологий.
     */
    @Transactional
    public List<Technology> findAll() {
        log.info("Получение списка всех технологий");
        return technologyRepository.findAll();
    }

    /**
     * Удаление технологии по идентификатору.
     */
    @Transactional
    public void delete(Long technologyId) {
        log.info("Удаление технологии по идентификатору {}", technologyId);
        if (technologyRepository.existsById(technologyId)) {
            technologyRepository.deleteById(technologyId);
        } else {
            throw new EntityNotFoundException(String.format("Технология с id %s не найдена", technologyId));
        }
    }
}