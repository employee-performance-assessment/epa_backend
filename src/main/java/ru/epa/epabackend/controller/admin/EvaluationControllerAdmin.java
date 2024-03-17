package ru.epa.epabackend.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.epa.epabackend.service.EvaluationService;

/**
 * Класс EvaluationControllerAdmin содержит эндпойнты для администратора, относящиеся к оценкам.
 *
 * @author Михаил Безуглов
 */
@Tag(name = "Admin: Оценки", description = "API администратора для работы с оценками")
@SecurityRequirement(name = "JWT")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/evaluation")
public class EvaluationControllerAdmin {

    private final EvaluationService evaluationService;




}
