package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
import ru.epa.epabackend.dto.technology.TechnologyResponseDto;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.repository.TechnologyRepository;
import ru.epa.epabackend.service.TechnologyService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс TechnologyServiceImpl содержит методы действий с технологией.
 *
 * @author Артем Масалкин
 */
@Service
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {
    private final TechnologyRepository technologyRepository;
    private final TechnologyMapper technologyMapper;

    /**
     * Добавление технологии.
     */
    @Transactional
    public TechnologyResponseDto create(TechnologyRequestDto technologyDto) {
        Technology technology = technologyRepository.save(technologyMapper.mapToEntity(technologyDto));
        return technologyMapper.mapToDto(technology);
    }

    /**
     * Получение технологии по идентификатору.
     */
    @Transactional
    public Technology findById(Long technologyId) {
        return technologyRepository.findById(technologyId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Объект класса %s не найден",
                        Technology.class)));
    }

    /**
     * Обновление технологии.
     */
    @Transactional
    public TechnologyResponseDto update(TechnologyRequestDto technologyDto, Long technologyId) {
        Technology oldTechnology = findById(technologyId);
        oldTechnology.setName(technologyDto.getName());
        return technologyMapper.mapToDto(oldTechnology);
    }

    /**
     * Получение списка всех технологий.
     */
    @Transactional
    public List<TechnologyResponseDto> findAll() {
        return technologyRepository.findAll().stream().map(technologyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаление технологии по идентификатору.
     */
    @Transactional
    public void delete(Long technologyId) {
        technologyRepository.deleteById(technologyId);
    }
}

