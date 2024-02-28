package ru.epa.epabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.TechnologyDto;
import ru.epa.epabackend.exception.NotFoundException;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.repository.TechnologyRepository;

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
    public TechnologyDto createTechnology(Technology technology) {
        return TechnologyMapper.toDto(technologyRepository.save(technology));
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
        return TechnologyMapper.toDto(getTechnologyById(technologyId));
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
