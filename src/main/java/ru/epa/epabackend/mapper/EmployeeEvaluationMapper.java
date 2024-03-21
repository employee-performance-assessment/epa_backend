package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationDto;
import ru.epa.epabackend.dto.evaluation.EmployeeEvaluationRequestDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.EmployeeEvaluation;
import ru.epa.epabackend.model.Evaluation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс EmployeeEvaluationMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring", uses = {EvaluationMapper.class, EmployeeMapper.class})
public interface EmployeeEvaluationMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    EmployeeEvaluationDto mapToDto(EmployeeEvaluation employeeEvaluation);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appraiser", source = "appraiser")
    @Mapping(target = "employee", source = "employee")
    @Mapping(target = "evaluation", source = "evaluation")
    @Mapping(target = "createDay", source = "createDay")
    EmployeeEvaluation mapToEntity(EmployeeEvaluationRequestDto evaluationRequestDto,
                                   Employee appraiser, Employee employee, Evaluation evaluation, LocalDate createDay);

    default List<EmployeeEvaluationDto> mapList(List<EmployeeEvaluation> employeeEvaluations) {
        return employeeEvaluations.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}
