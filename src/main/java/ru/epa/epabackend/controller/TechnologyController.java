package ru.epa.epabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.TechnologyDto;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.service.TechnologyService;

import java.util.List;

/**
 * Класс TechnologyController содержит ендпоинты для технологии.
 *
 * @author Артем Масалкин
 */

@Validated
@RestController
@RequestMapping(path = "/technologies")
@RequiredArgsConstructor
public class TechnologyController {
    private final TechnologyService technologyService;

    /**
     * Ендпоинт создания технологии.
     */
    @PostMapping
    public TechnologyDto createTechnology(@RequestBody TechnologyDto technologyDto) {
        return technologyService.createTechnology(TechnologyMapper.toEntity(technologyDto));
    }

    /**
     * Ендпоинт обновления технологии.
     */
    @PatchMapping("/{technologyId}")
    public TechnologyDto updateTechnology(@RequestBody TechnologyDto technologyDto,
                                          @PathVariable("technologyId") Long technologyId) {
        return technologyService.updateTechnology(technologyDto, technologyId);
    }

    /**
     * Эндпоинт выведения списка всех технологий.
     */
    @GetMapping
    public List<TechnologyDto> getAllTechnologies() {
        return technologyService.getAllTechnologies();
    }

    /**
     * Эндпоинт удаления технологии.
     */
    @GetMapping("/{technologyId}")
    public void deleteTechnologyById(@PathVariable Long technologyId) {
        technologyService.deleteTechnologyById(technologyId);
    }
}
