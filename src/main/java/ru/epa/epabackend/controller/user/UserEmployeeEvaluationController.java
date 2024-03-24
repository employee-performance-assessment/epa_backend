package ru.epa.epabackend.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.mapper.EmployeeEvaluationMapper;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.service.EmployeeEvaluationService;

import java.util.List;

/**
 * Класс EmployeeEvaluationControllerUser содержит эндпойнты для авторизованного пользователя, относящиеся к
 * оценке сотрудником коллег и получению своих оценок.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Private: Оценки Сотрудников", description = "Закрытый API для работы с оценками сотрудников")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/evaluations")
public class UserEmployeeEvaluationController {

    private final EmployeeEvaluationService employeeEvaluationService;
    private final EmployeeEvaluationMapper employeeEvaluationMapper;

    /**
     * Эндпойнт добавления оценок сотрудника
     */
    @Operation(
            summary = "Сохранения оценок сотрудника своего коллеги",
            description = "При успешном добавлении возвращается код 201 Created."
    )
    @PostMapping("/{evaluatedId}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeEvaluationDto save(@PathVariable("evaluatedId") Long evaluatedId,
                                      @RequestParam(required = true) Long evaluatorId,
                                      @Valid @RequestBody EmployeeEvaluationRequestDto employeeEvaluationRequestDto) {
        EmployeeEvaluation employeeEvaluation = employeeEvaluationService
                .create(evaluatorId, evaluatedId, employeeEvaluationRequestDto);
        return employeeEvaluationMapper.mapToDto(employeeEvaluation);
    }

    /**
     * Эндпойнт получения сотрудником всех оценок коллег о себе.
     */
    @Operation(
            summary = "Получение сотрудником всех оценок коллег о себе",
            description = "Возвращает список всех оценок коллег о себе (усредненный по оценкам)" +
                    "\n\nВ случае, если не найдено ни одной оценке, возвращает пустой список."
    )
    @GetMapping("/{evaluatedId}")
    public List<EmployeeEvaluationDto> findAllByAppraiserId(@PathVariable @Parameter(required = true) Long evaluatedId) {
        List<EmployeeEvaluation> employeeEvaluations = employeeEvaluationService.findAllByAppraiserId(evaluatedId);
        return employeeEvaluationMapper.mapList(employeeEvaluations);
    }
}