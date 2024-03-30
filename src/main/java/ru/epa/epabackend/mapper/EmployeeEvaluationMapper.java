package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationResponseFullDto;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;

import java.util.List;

/**
 * Класс EmployeeEvaluationMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring", uses = {CriteriaMapper.class, EmployeeMapper.class})
public interface EmployeeEvaluationMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    EmployeeEvaluationResponseFullDto mapToDto(EmployeeEvaluation employeeEvaluation);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    EmployeeEvaluation mapToEntity(EmployeeEvaluationRequestDto evaluationRequestDto,
                                   Employee evaluated, Employee evaluator, Criteria criteria);

    /**
     * Преобразование списка сущностей в список DTO.
     */
    List<EmployeeEvaluationResponseFullDto> mapList(List<EmployeeEvaluation> employeeEvaluations);
}
