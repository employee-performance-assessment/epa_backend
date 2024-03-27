package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.analytics.IndividualAnalyticsResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsFullResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsShortResponseDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс AnalyticsService содержит методы для аналитики задач и оценок .
 *
 * @author Владислав Осипов
 */
public interface AnalyticsService {

    /**
     * Получение командной статистики для админа
     */
    TeamAnalyticsFullResponseDto findTeamStatisticsByAdmin(LocalDate startDate, LocalDate endDate, String email);

    /**
     * Получение индивидуальной статистики для админа
     */
    List<IndividualAnalyticsResponseDto> findIndividualStatisticsByAdmin(LocalDate startDate, LocalDate endDate,
                                                                         String email);

    /**
     * Получение командной статистики для сотрудника
     */
    TeamAnalyticsShortResponseDto findTeamStatistics(LocalDate startDate, LocalDate endDate, String email);

    /**
     * Получение индивидуальной статистики для сотрудника
     */
    IndividualAnalyticsResponseDto findIndividualStatistics(LocalDate startDate, LocalDate endDate, String email);
}
