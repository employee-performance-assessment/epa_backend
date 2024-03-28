package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.evaluation.CriteriaDto;
import ru.epa.epabackend.dto.evaluation.CriteriaRequestDto;
import ru.epa.epabackend.model.Criteria;

import java.util.List;

/**
 * Класс EvaluationMapper содержит преобразование критериев из сущности в Dto и наоборот.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring")
public interface CriteriaMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    CriteriaDto mapToDto(Criteria criteria);

    /**
     * Преобразование из DTO в сущность.
     */
    Criteria mapToEntity(CriteriaRequestDto criteriaRequestDto);

    List<CriteriaDto> mapList(List<Criteria> criteria);
}
