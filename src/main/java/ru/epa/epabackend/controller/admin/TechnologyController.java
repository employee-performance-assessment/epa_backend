package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.technology.TechnologyDto;
import ru.epa.epabackend.service.TechnologyService;

import java.util.List;

/**
 * Класс TechnologyController содержит ендпоинты для технологии.
 *
 * @author Артем Масалкин
 */

@Validated
@RestController
@RequestMapping("/admin/technologies")
@RequiredArgsConstructor
@Tag(name = "Admin: Технологии", description = "API для работы с технологиями")
public class TechnologyController {
    private final TechnologyService technologyService;

    /**
     * Эндпоинт создания технологии.
     */
    @Operation(
            summary = "Создание новой технологии"
    )
    @PostMapping
    public TechnologyDto createTechnology(@RequestBody TechnologyDto technologyDto) {
        return technologyService.createTechnology(technologyDto);
    }

    /**
     * Эндпоинт обновления технологии.
     */
    @Operation(
            summary = "Обновление технологии",
            description = "Обновляет технологию, если она существует в базе данных."
    )
    @PatchMapping("/{technologyId}")
    public TechnologyDto updateTechnology(@RequestBody TechnologyDto technologyDto,
                                          @PathVariable("technologyId") Long technologyId) {
        return technologyService.updateTechnology(technologyDto, technologyId);
    }

    /**
     * Эндпоинт выведения списка всех технологий.
     */
    @Operation(
            summary = "Возвращает список всех технологий"
    )
    @GetMapping
    public List<TechnologyDto> getAllTechnologies() {
        return technologyService.getAllTechnologies();
    }

    /**
     * Эндпоинт удаления технологии.
     */
    @Operation(
            summary = "Удаляет технологию",
            description = "Удаляет технологию, если она существует в базе данных."
    )
    @DeleteMapping("/{technologyId}")
    public void deleteTechnologyById(@PathVariable Long technologyId) {
        technologyService.deleteTechnologyById(technologyId);
    }
}
