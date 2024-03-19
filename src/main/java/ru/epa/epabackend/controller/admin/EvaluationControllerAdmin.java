package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.evaluation.EvaluationDto;
import ru.epa.epabackend.dto.evaluation.EvaluationRequestDto;
import ru.epa.epabackend.mapper.EvaluationMapper;
import ru.epa.epabackend.service.EvaluationService;

import java.util.List;

/**
 * Класс EvaluationControllerAdmin содержит эндпойнты для администратора, относящиеся к критериям оценок.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Оценки", description = "API администратора для работы с критериями оценок")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/evaluation")
public class EvaluationControllerAdmin {

    private final EvaluationService evaluationService;
    private final EvaluationMapper evaluationMapper;

    /**
     * Эндпойнт добавления нового критерия оценки
     */
    @Operation(
            summary = "Добавление нового критерия оценки",
            description = "При успешном добавлении возвращается код 201 Created."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluationDto save(@RequestBody EvaluationRequestDto evaluationRequestDto) {
        return evaluationMapper.mapToDto(evaluationService.create(evaluationRequestDto));
    }

    /**
     * Эндпойнт поиска всех оценок.
     */
    @Operation(
            summary = "Получение всех критериев оценок",
            description = "Возвращает список всех критериев оценок." +
                    "В случае, если не найдено ни одного критерия оценки, возвращает пустой список."
    )
    @GetMapping
    public List<EvaluationDto> findAll() {
        return evaluationMapper.mapList(evaluationService.findAll());
    }

    /**
     * Эндпойнт поиска названия оценки по её ID.
     */
    @Operation(
            summary = "Получение информации о названии критерия оценки",
            description = "Возвращает название критерия оценки и её ID, если она существует в базе данных. " +
                    "В случае, если критерия оценки не найдено, возвращает ошибку 404"
    )
    @GetMapping("/{evaluationId}")
    public EvaluationDto findById(@Parameter(required = true) @PathVariable Long evaluationId) {
        return evaluationMapper.mapToDto(evaluationService.findById(evaluationId));
    }
}
