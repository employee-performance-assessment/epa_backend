package ru.epa.epabackend.mapper;

import org.mapstruct.*;
import ru.epa.epabackend.dto.criteria.CriteriaResponseDto;
import ru.epa.epabackend.dto.criteria.CriteriaRequestDto;
import ru.epa.epabackend.model.Criteria;

import java.util.List;

/**
 * Класс EvaluationMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring")
public interface CriteriaMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    CriteriaResponseDto mapToDto(Criteria criteria);

    /**
     * Преобразование из DTO в сущность.
     */
    Criteria mapToEntity(CriteriaRequestDto criteriaRequestDto);

    /**
     * Преобразование списка сущностей в список DTO.
     */
    List<CriteriaResponseDto> mapList(List<Criteria> criteria);

    /**
     * Преобразование списка DTO в список сущностей.
     */
    List<Criteria> mapListToEntity(List<CriteriaRequestDto> criteriaRequestDtoList);
}
