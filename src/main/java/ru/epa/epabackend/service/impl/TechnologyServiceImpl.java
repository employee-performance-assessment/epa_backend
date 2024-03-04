package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.technology.TechnologyDto;
import ru.epa.epabackend.exception.exceptions.NotFoundException;
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
public class TechnologyServiceImpl implements TechnologyService {
    private final TechnologyRepository technologyRepository;

    /**
     * Добавление технологии.
     */
    @Transactional
    public TechnologyDto createTechnology(TechnologyDto technologyDto) {
        Technology technology = technologyRepository.save(TechnologyMapper.toEntity(technologyDto));
        return TechnologyMapper.toDto(technology);
    }

    /**
     * Получение технологии по идентификатору.
     */
    @Transactional
    public Technology getTechnologyById(Long technologyId) {
        return technologyRepository.findById(technologyId)
                .orElseThrow(() -> new NotFoundException(String.format("Технологии с id %d не существует", technologyId)));
    }

    /**
     * Обновление технологии.
     */
    @Transactional
    public TechnologyDto updateTechnology(TechnologyDto technologyDto, Long technologyId) {
        Technology oldTechnology = getTechnologyById(technologyId);
        oldTechnology.setName(technologyDto.getName());
        return TechnologyMapper.toDto(oldTechnology);
    }

    /**
     * Получение списка всех технологий.
     */
    @Transactional
    public List<TechnologyDto> getAllTechnologies() {
        return TechnologyMapper.toTechnologyDtoList(technologyRepository.findAll());
    }

    /**
     * Удаление технологии по идентификатору.
     */
    @Transactional
    public void deleteTechnologyById(Long technologyId) {
        technologyRepository.deleteById(technologyId);
    }
}
