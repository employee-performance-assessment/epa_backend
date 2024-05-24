package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;
import ru.epa.epabackend.dto.criteria.ResponseCriteriaDto;
import ru.epa.epabackend.model.Criteria;

import java.util.List;

/**
 * Класс EvaluationMapper содержит преобразование сущности
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring")
public interface CriteriaMapper {

    /**
     * Преобразование из сущности в DTO
     */
    ResponseCriteriaDto mapToDto(Criteria criteria);

    /**
     * Преобразование из DTO в сущность
     */
    Criteria mapToEntity(RequestCriteriaDto requestCriteriaDto);

    /**
     * Преобразование списка сущностей в список DTO
     */
    List<ResponseCriteriaDto> mapList(List<Criteria> criteria);

    /**
     * Преобразование списка DTO в список сущностей
     */
    List<Criteria> mapListToEntity(List<RequestCriteriaDto> requestCriteriaDtoList);
}
