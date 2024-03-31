package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.analytics.ResponseIndividualAnalyticsDto;
import ru.epa.epabackend.dto.analytics.ResponseTeamAnalyticsFullDto;
import ru.epa.epabackend.dto.analytics.ResponseTeamAnalyticsShortDto;
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
    ResponseIndividualAnalyticsDto mapToEntityIndividual(IndividualAnalytics individualAnalytics);

    List<ResponseIndividualAnalyticsDto> mapList(List<IndividualAnalytics> individualAnalytics);

    /**
     * Преобразование из сущности в полное DTO
     */
    ResponseTeamAnalyticsFullDto mapToFullDto(TeamAnalytics teamAnalytics);

    /**
     * Преобразование из сущности в кракое DTO
     */
    ResponseTeamAnalyticsShortDto mapToShortDto(TeamAnalytics teamAnalytics);
}