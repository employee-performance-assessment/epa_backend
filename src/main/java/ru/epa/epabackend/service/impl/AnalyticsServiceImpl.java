package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс AnalyticServiceImpl содержит методы для аналитики задач и оценок.
 *
 * @author Владислав Осипов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;
    @Value("${percentage_of_tasks_completed_on_time:80}")
    private int leaderThresHold;
    @Value("${percentage_of_tasks_delayed:60}")
    private int violatorThresHold;

    /**
     * Получение командной статистики для админа
     */
    @Override
    @Transactional(readOnly = true)
    public TeamAnalytics getTeamStatsByAdmin(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        log.info("Получение командной статистики для админа");
        int teamCompletedOnTime = 0;
        int teamDelayed = 0;
        List<Employee> leaders = new ArrayList<>();
        List<Employee> deadlineViolators = new ArrayList<>();
        List<Task> allTasks = taskRepository.findAllByOwnerEmailAndFinishDateBetween(email, rangeStart, rangeEnd);
        Map<Employee, List<Task>> employeeTasks = allTasks.stream().collect(Collectors.groupingBy(Task::getExecutor));

        for (Map.Entry<Employee, List<Task>> entry : employeeTasks.entrySet()) {
            List<Task> tasks = entry.getValue();
            int delayed = countDelayedTasks(tasks);
            int completedOnTime = tasks.size() - delayed;
            if (!tasks.isEmpty()) {
                if (calcPercent(completedOnTime, tasks.size()) > leaderThresHold) {
                    leaders.add(entry.getKey());
                } else {
                    if (calcPercent(delayed, tasks.size()) > violatorThresHold) {
                        deadlineViolators.add(entry.getKey());
                    }
                }
                teamCompletedOnTime += completedOnTime;
                teamDelayed += delayed;
            }
        }

        int totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        return TeamAnalytics.builder()
                .completedOnTimePercent(calcPercent(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
                .delayedPercent(calcPercent(teamDelayed, totalNumbersOfTaskOfTeamCompleted))
                .leaders(leaders)
                .deadlineViolators(deadlineViolators)
                .build();
    }

    /**
     * Получение индивидуальной статистики для админа
     */
    @Override
    @Transactional(readOnly = true)
    public List<IndividualAnalytics> getIndividualStatsByAdmin(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        log.info("Получение индивидуальной статистики для админа");
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
        log.info("Получение командной статистики для сотрудника");
        Employee employee = employeeService.findByEmail(email);
        List<Task> tasks = taskRepository.findAllByOwnerIdAndFinishDateBetween(employee.getCreator().getId(),
                rangeStart, rangeEnd);
        int teamDelayed = countDelayedTasks(tasks);
        int teamCompletedOnTime = tasks.size() - teamDelayed;
        int totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        return TeamAnalytics.builder()
                .completedOnTimePercent(calcPercent(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
                .delayedPercent(calcPercent(teamDelayed, totalNumbersOfTaskOfTeamCompleted))
                .build();
    }

    /**
     * Получение индивидуальной статистики для сотрудника
     */
    @Override
    @Transactional(readOnly = true)
    public IndividualAnalytics getIndividualStats(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        log.info("Получение индивидуальной статистики для сотрудника");
        Employee employee = employeeService.findByEmail(email);
        return getIndividualStats(employee, rangeStart, rangeEnd);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer findQuantityOfPointsPerMonth(Long employeeId, LocalDate rangeStart, LocalDate rangeEnd) {
        return taskRepository.getSumPointsByExecutorIdAndThisMonth(employeeId, rangeStart, rangeEnd);
    }

    private IndividualAnalytics getIndividualStats(Employee employee, LocalDate rangeStart, LocalDate rangeEnd) {
        List<Task> tasks = taskRepository.findAllByExecutorIdAndFinishDateBetween(employee.getId(), rangeStart, rangeEnd);
        int delayed = countDelayedTasks(tasks);
        int completedOnTime = tasks.size() - delayed;
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

    private int countDelayedTasks(List<Task> tasks) {
        return (int) tasks.stream()
                .filter(task -> task.getFinishDate().isAfter(task.getDeadLine()))
                .count();
    }
}