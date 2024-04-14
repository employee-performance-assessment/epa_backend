package ru.epa.epabackend.service;

import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.TeamAnalytics;

import java.security.Principal;
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
    TeamAnalytics getTeamStatsByAdmin(LocalDate startDate, LocalDate endDate, String email);

    /**
     * Получение индивидуальной статистики для админа
     */
    List<IndividualAnalytics> getIndividualStatsByAdmin(LocalDate startDate, LocalDate endDate,
                                                        String email);

    /**
     * Получение командной статистики для сотрудника
     */
    TeamAnalytics getTeamStats(LocalDate startDate, LocalDate endDate, String email);

    /**
     * Получение индивидуальной статистики для сотрудника
     */
    IndividualAnalytics getIndividualStats(LocalDate startDate, LocalDate endDate, String email);

    /**
     * Получение суммы баллов по выполненным задачам сотрудника за текущий месяц.
     */
    Integer findQuantityOfPointsByAdmin(Long employeeId, LocalDate rangeStart, LocalDate rangeEnd, String email);

    @Transactional(readOnly = true)
    Integer findQuantityOfPointsByUser(Principal principal, LocalDate rangeStart, LocalDate rangeEnd);
}