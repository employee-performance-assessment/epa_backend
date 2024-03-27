package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.model.TeamAnalytics;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.AnalyticsService;
import ru.epa.epabackend.service.EmployeeService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс AnalyticServiceImpl содержит методы для аналитики задач и оценок.
 *
 * @author Владислав Осипов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;
    @Value("${percentage_of_tasks_completed_on_time:80}")
    private int leaderTrashHold;
    @Value("${percentage_of_tasks_delayed:60}")
    private int violatorTrashHold;

    /**
     * Получение командной статистики для админа
     */
    @Override
    @Transactional(readOnly = true)
    public TeamAnalytics getTeamStatsByAdmin(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        int teamCompletedOnTime = 0;
        int teamDelayed = 0;
        List<Employee> leaders = new ArrayList<>();
        List<Employee> deadlineViolators = new ArrayList<>();
        List<Employee> employees = employeeService.findAllByCreatorEmail(email);
        for (Employee employee : employees) {
            List<Task> tasks = taskRepository
                    .findAllByExecutorIdAndFinishDateBetween(employee.getId(), rangeStart, rangeEnd);
            int delayed = calcDelayedTasks(tasks);
            int completedOnTime = tasks.size() - delayed;
            if (!tasks.isEmpty()) {
                if (calcPercent(completedOnTime, tasks.size()) > leaderTrashHold) {
                    leaders.add(employee);
                }
                if (delayed != 0 && calcPercent(delayed, tasks.size()) > violatorTrashHold) {
                    deadlineViolators.add(employee);
                }
                teamCompletedOnTime += completedOnTime;
                teamDelayed += delayed;
            }
        }

        int totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return TeamAnalytics.builder()
                    .completedOnTimePercent(calcPercent(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
                    .delayedPercent(calcPercent(teamDelayed, totalNumbersOfTaskOfTeamCompleted))
                    .leaders(leaders)
                    .deadlineViolators(deadlineViolators)
                    .build();
        }
        return new TeamAnalytics();
    }

    /**
     * Получение индивидуальной статистики для админа
     */
    @Override
    @Transactional(readOnly = true)
    public List<IndividualAnalytics> getIndividualStatsByAdmin(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        List<IndividualAnalytics> employeesShortDto = new ArrayList<>();
        List<Employee> employees = employeeService.findAllByCreatorEmail(email);
        for (Employee employee : employees) {
            IndividualAnalytics individualAnalytics = getIndividualStats(employee, rangeStart, rangeEnd);
            employeesShortDto.add(individualAnalytics);
        }
        return employeesShortDto;
    }

    /**
     * Получение командной статистики для сотрудника
     */
    @Override
    @Transactional(readOnly = true)
    public TeamAnalytics getTeamStats(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        Employee employee = employeeService.findByEmail(email);
        List<Task> tasks = taskRepository.findAllByOwnerIdAndFinishDateBetween(employee.getCreator().getId(),
                rangeStart, rangeEnd);
        int teamDelayed = calcDelayedTasks(tasks);
        int teamCompletedOnTime = tasks.size() - teamDelayed;
        int totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return TeamAnalytics.builder()
                    .completedOnTimePercent(calcPercent(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
                    .delayedPercent(calcPercent(teamDelayed, totalNumbersOfTaskOfTeamCompleted))
                    .build();
        }
        return new TeamAnalytics();
    }

    /**
     * Получение индивидуальной статистики для сотрудника
     */
    @Override
    @Transactional(readOnly = true)
    public IndividualAnalytics getIndividualStats(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        Employee employee = employeeService.findByEmail(email);
        return getIndividualStats(employee, rangeStart, rangeEnd);
    }

    private IndividualAnalytics getIndividualStats(Employee employee, LocalDate rangeStart, LocalDate rangeEnd) {
        int completedOnTime = 0;
        int delayed = 0;
        List<Task> tasks = taskRepository.findAllByExecutorIdAndFinishDateBetween(employee.getId(), rangeStart, rangeEnd);
        for (Task task : tasks) {
            if (task.getFinishDate().isAfter(task.getDeadLine())) {
                delayed++;
            } else {
                completedOnTime++;
            }

        }

        int totalNumberOfTaskOfEmployeeCompleted = completedOnTime + delayed;
        return IndividualAnalytics.builder()
                .employeeId(employee.getId())
                .employeeFullName(employee.getFullName())
                .employeePosition(employee.getPosition())
                .completedOnTimePercent(calcPercent(completedOnTime, totalNumberOfTaskOfEmployeeCompleted))
                .delayedPercent(calcPercent(delayed, totalNumberOfTaskOfEmployeeCompleted))
                .build();
    }

    private int calcPercent(int number1, int number2) {
        return number2 == 0 ? 0 : number1 * 100 / number2;
    }

    private int calcDelayedTasks(List<Task> tasks) {
        int delayed = 0;
        for (Task task : tasks) {
            if (task.getFinishDate().isAfter(task.getDeadLine())) {
                delayed++;
            }
        }
        return delayed;
    }
}