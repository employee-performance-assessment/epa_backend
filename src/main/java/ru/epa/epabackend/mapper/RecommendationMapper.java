package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.recommendation.RecommendationRequestDto;
import ru.epa.epabackend.dto.recommendation.RecommendationResponseDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Recommendation;

import java.util.List;

/**
 * Класс RecommendationMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface RecommendationMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    RecommendationResponseDto mapToDto(Recommendation recommendation);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    Recommendation mapToEntity(RecommendationRequestDto recommendationRequestDto,
                               Employee recipient, Employee sender);

    /**
     * Преобразование списка сущностей в список DTO.
     */
    List<RecommendationResponseDto> mapList(List<Recommendation> recommendations);
}
