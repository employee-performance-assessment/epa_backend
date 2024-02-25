package ru.epa.epabackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.TechnologyDto;
import ru.epa.epabackend.mapper.TechnologyMapper;
import ru.epa.epabackend.service.TechnologyService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/technologys")
@RequiredArgsConstructor
public class TechnologyController {
    private final TechnologyService technologyService;

    @PostMapping
    public TechnologyDto createTechnology(@RequestBody TechnologyDto technologyDto) {
        return technologyService.createTechnology(TechnologyMapper.toTechnology(technologyDto));
    }

    @PatchMapping("/{technologyId}")
    public TechnologyDto updateTechnology(@RequestBody TechnologyDto technologyDto,
                                          @PathVariable("technologyId") Long technologyId) {
        return technologyService.updateTechnology(technologyDto, technologyId);
    }

    @GetMapping
    public List<TechnologyDto> getAllTechnology() {
        return technologyService.getAllTechnologys();
    }

    @GetMapping("/{technologyId}")
    public TechnologyDto getTechnologyById(@PathVariable Long technologyId) {
        return technologyService.getTechnologyDtoById(technologyId);
    }

    @GetMapping("/{technologyId}")
    public void deleteTechnologyById(@PathVariable Long technologyId) {
        technologyService.deleteTechnologyById(technologyId);
    }
}
