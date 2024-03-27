package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import ru.epa.epabackend.dto.analytics.IndividualAnalyticsResponseDto;
import ru.epa.epabackend.model.IndividualAnalytics;

import java.util.List;

/**
 * Интерфейс AnalyticsMapper содержит преобразование сущности.
 *
 * @author Владислав Осипов
 */
@Mapper(componentModel = "spring")
public interface AnalyticsIndividualMapper {

    /**
     * Преобразование из сущности в Dto.
     */
    IndividualAnalyticsResponseDto mapToEntityIndividual(IndividualAnalytics individualAnalytics);

    List<IndividualAnalyticsResponseDto> mapList(List<IndividualAnalytics> individualAnalytics);
}