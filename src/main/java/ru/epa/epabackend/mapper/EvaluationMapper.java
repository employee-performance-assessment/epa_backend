package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.evaluation.EvaluationDto;
import ru.epa.epabackend.dto.evaluation.EvaluationRequestDto;
import ru.epa.epabackend.model.Evaluation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс EvaluationMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring")
public interface EvaluationMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    EvaluationDto mapToDto(Evaluation evaluation);

    /**
     * Преобразование из DTO в сущность.
     */
    Evaluation mapToEntity(EvaluationRequestDto evaluationRequestDto);

    default List<EvaluationDto> mapList(List<Evaluation> evaluations) {
        return evaluations.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
