package ru.epa.epabackend.service;

import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.TeamAnalytics;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * Интерфейс AnalyticsService содержит методы для аналитики задач и оценок
 *
 * @author Владислав Осипов
 */
public interface AnalyticsService {

    /**
     * Получение командной статистики для админа
     */
    TeamAnalytics getTeamStatsByAdmin(Integer year, Integer month, String email);

    /**
     * Получение индивидуальной статистики для админа
     */
    List<IndividualAnalytics> getIndividualStatsByAdmin(Integer year, Integer month,
                                                        String email);

    /**
     * Получение командной статистики для сотрудника
     */
    TeamAnalytics getTeamStats(Integer year, Integer month, String email);

    /**
     * Получение индивидуальной статистики для сотрудника
     */
    IndividualAnalytics getIndividualStats(Integer year, Integer month, String email);

    /**
     * Получение суммы баллов по выполненным задачам сотрудника за текущий месяц
     */
    Integer findQuantityOfPointsByAdmin(Long employeeId, LocalDate rangeStart, LocalDate rangeEnd, String email);

    @Transactional(readOnly = true)
    Integer findQuantityOfPointsByUser(Principal principal, LocalDate rangeStart, LocalDate rangeEnd);

    List<Integer> findYearsForTeamStatistics(String email);

    List<Integer> findMonthsForTeamStatistics(Integer year, String email);

    List<Integer> findYearsForIndividualStatistics(String email);

    List<Integer> findMonthsForIndividualStatistics(Integer year, String email);
}