package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.evaluation.RequestEmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationFullDto;
import ru.epa.epabackend.dto.evaluation.ResponseEmployeeEvaluationShortDto;
import ru.epa.epabackend.model.*;

import java.util.List;

/**
 * Класс EmployeeEvaluationMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring", uses = {CriteriaMapper.class, EmployeeMapper.class, QuestionnaireMapper.class})
public interface EmployeeEvaluationMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    ResponseEmployeeEvaluationFullDto mapToDto(EmployeeEvaluation employeeEvaluation);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    EmployeeEvaluation mapToEntity(RequestEmployeeEvaluationDto requestEmployeeEvaluationDto, Employee evaluated,
                                   Questionnaire questionnaire, Employee evaluator, Criteria criteria);

    /**
     * Преобразование списка сущностей в список DTO.
     */
    List<ResponseEmployeeEvaluationFullDto> mapList(List<EmployeeEvaluation> employeeEvaluations);

    /**
     * Преобразование из сущности в DTO.
     */
    @Mapping(target = "name", source = "employeeEvaluation.criteria.name")
    ResponseEmployeeEvaluationShortDto mapToShortDto(EmployeeEvaluation employeeEvaluation);

    List<ResponseEmployeeEvaluationShortDto> mapToShortListDto(List<EmployeeEvaluation> evaluations);
}
