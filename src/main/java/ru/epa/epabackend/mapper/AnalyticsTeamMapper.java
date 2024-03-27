package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsFullResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsShortResponseDto;
import ru.epa.epabackend.model.TeamAnalytics;

/**
 * Интерфейс AnalyticsMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */
@Mapper(componentModel = "spring")
public interface AnalyticsTeamMapper {

    /**
     * Преобразование из сущности в полное DTO
     */
    TeamAnalyticsFullResponseDto mapToFullDto(TeamAnalytics teamAnalytics);

    /**
     * Преобразование из сущности в кракое DTO
     */
    TeamAnalyticsShortResponseDto mapToShortDto(TeamAnalytics teamAnalytics);
}