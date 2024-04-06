package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.recommendation.ResponseRecommendationDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.model.Recommendation;

import java.util.List;

/**
 * Класс RecommendationMapper содержит преобразование сущности.
 *
 * @author Михаил Безуглов
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, QuestionnaireMapper.class})
public interface RecommendationMapper {

    /**
     * Преобразование из сущности в DTO.
     */
    ResponseRecommendationDto mapToDto(Recommendation recommendation);

    /**
     * Преобразование из DTO в сущность.
     */
    @Mapping(target = "id", ignore = true)
    Recommendation mapToEntity(String recommendation, Questionnaire questionnaire,
                               Employee recipient, Employee sender);

    /**
     * Преобразование списка сущностей в список DTO.
     */
    List<ResponseRecommendationDto> mapList(List<Recommendation> recommendations);
}
