package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.analytics.IndividualAnalyticsResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsFullResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsShortResponseDto;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.TeamAnalytics;

import java.util.List;

/**
 * Интерфейс AnalyticsMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface AnalyticsMapper {

    /**
     * Преобразование из сущности в Dto.
     */
    IndividualAnalyticsResponseDto mapToEntityIndividual(IndividualAnalytics individualAnalytics);

    List<IndividualAnalyticsResponseDto> mapList(List<IndividualAnalytics> individualAnalytics);

    /**
     * Преобразование из сущности в полное DTO
     */
    TeamAnalyticsFullResponseDto mapToFullDto(TeamAnalytics teamAnalytics);

    /**
     * Преобразование из сущности в кракое DTO
     */
    TeamAnalyticsShortResponseDto mapToShortDto(TeamAnalytics teamAnalytics);
}