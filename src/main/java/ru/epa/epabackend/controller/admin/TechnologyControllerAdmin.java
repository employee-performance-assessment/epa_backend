package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.technology.TechnologyRequestDto;
import ru.epa.epabackend.dto.technology.TechnologyResponseDto;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.model.Technology;
import ru.epa.epabackend.service.TechnologyService;

import java.util.List;

/**
 * Класс TechnologyController содержит эндпойнты для администратора, относящиеся к технологиям.
 *
 * @author Артем Масалкин
 */

@Validated
@RestController
@RequestMapping("/admin/technologies")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
@Tag(name = "Admin: Технологии", description = "API для работы с технологиями")
public class TechnologyControllerAdmin {
    private final TechnologyService technologyService;
    private final TechnologyMapper technologyMapper;

    /**
     * Эндпойнт создания технологии.
     */
    @Operation(
            summary = "Создание новой технологии"
    )
    @PostMapping
    public TechnologyResponseDto createTechnology(@RequestBody TechnologyRequestDto technologyRequestDto) {
        return technologyMapper.mapToDto(technologyService.create(technologyRequestDto));
    }

    /**
     * Эндпойнт обновления технологии.
     */
    @Operation(
            summary = "Обновление технологии",
            description = "Обновляет технологию, если она существует в базе данных."
    )
    @PatchMapping("/{technologyId}")
    public TechnologyResponseDto updateTechnology(
            @RequestBody TechnologyRequestDto technologyRequestDto,
            @PathVariable("technologyId") Long technologyId) {
        return technologyMapper.mapToDto(technologyService.update(technologyRequestDto, technologyId));
    }

    /**
     * Эндпойнт выведения списка всех технологий.
     */
    @Operation(
            summary = "Возвращает список всех технологий"
    )
    @GetMapping
    public List<TechnologyResponseDto> getAllTechnologies() {
        List<Technology> technologies = technologyService.findAll();
        return technologyMapper.mapList(technologies);
    }

    /**
     * Эндпойнт удаления технологии.
     */
    @Operation(
            summary = "Удаляет технологию",
            description = "Удаляет технологию, если она существует в базе данных."
    )
    @DeleteMapping("/{technologyId}")
    public void deleteTechnologyById(@PathVariable Long technologyId) {
        technologyService.delete(technologyId);
    }
}
