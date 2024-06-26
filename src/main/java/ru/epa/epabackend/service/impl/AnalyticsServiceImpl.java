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

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
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
    public TeamAnalytics getTeamStatsByAdmin(Integer year, Integer month, String email) {
        log.info("Получение командной статистики для админа");
        int teamCompletedOnTime = 0;
        int teamDelayed = 0;
        List<Employee> leaders = new ArrayList<>();
        List<Employee> deadlineViolators = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<Task> allTasks = taskRepository.findAllByOwnerEmailAndFinishDateBetween(email, startDate, endDate);
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
    public List<IndividualAnalytics> getIndividualStatsByAdmin(Integer year, Integer month, String email) {
        log.info("Получение индивидуальной статистики для админа");
        List<IndividualAnalytics> employeesShortDto = new ArrayList<>();
        List<Employee> employees = employeeService.findAllByCreatorEmail(email);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        for (Employee employee : employees) {
            if (employee.getCreated().isBefore(startDate)) {
                IndividualAnalytics individualAnalytics = getIndividualStats(employee, startDate, endDate);
                employeesShortDto.add(individualAnalytics);
            }
        }
        return employeesShortDto;
    }

    /**
     * Получение командной статистики для сотрудника
     */
    @Override
    @Transactional(readOnly = true)
    public TeamAnalytics getTeamStats(Integer year, Integer month, String email) {
        log.info("Получение командной статистики для сотрудника");
        Employee employee = employeeService.findByEmail(email);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        List<Task> tasks = taskRepository.findAllByOwnerIdAndFinishDateBetween(employee.getCreator().getId(),
                startDate, endDate);
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
    public IndividualAnalytics getIndividualStats(Integer year, Integer month, String email) {
        log.info("Получение индивидуальной статистики для сотрудника");
        Employee employee = employeeService.findByEmail(email);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return getIndividualStats(employee, startDate, endDate);
    }

    /**
     * Получение админом суммы баллов по выполненным задачам сотрудника за текущий месяц.
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findQuantityOfPointsByAdmin(Long employeeId, LocalDate rangeStart, LocalDate rangeEnd, String email) {
        log.info("Получение админом суммы баллов по выполненным задачам сотрудника за текущий месяц");
        Employee employee = employeeService.findById(employeeId);
        Employee admin = employeeService.findByEmail(email);
        employeeService.checkAdminForEmployee(admin, employee);
        Integer points = taskRepository.getSumPointsByExecutorIdAndForCurrentMonth(employeeId, rangeStart, rangeEnd);
        return points == null ? 0 : points;
    }

    /**
     * Получение сотрудником суммы своих баллов по выполненным задачам за текущий месяц.
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findQuantityOfPointsByUser(Principal principal, LocalDate rangeStart, LocalDate rangeEnd) {
        log.info("Получение сотрудником суммы своих баллов по выполненным задачам за текущий месяц");
        Employee employee = employeeService.findByEmail(principal.getName());
        Integer points = taskRepository.getSumPointsByExecutorIdAndForCurrentMonth(employee.getId(), rangeStart, rangeEnd);
        return points == null ? 0 : points;
    }

    @Override
    public List<Integer> findYearsForTeamStatistics(String email) {
        Employee employee = employeeService.findByEmail(email);
        Employee admin = employee.getCreator() == null ? employee : employee.getCreator();
        return taskRepository.findAllByOwnerEmail(admin.getEmail()).stream()
                .filter(t -> t.getFinishDate() != null)
                .map(t -> t.getFinishDate().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> findMonthsForTeamStatistics(Integer year, String email) {
        Employee employee = employeeService.findByEmail(email);
        Employee admin = employee.getCreator() == null ? employee : employee.getCreator();
        return taskRepository.findAllByOwnerEmail(admin.getEmail()).stream()
                .filter(t -> t.getFinishDate() != null && t.getFinishDate().getYear() == year)
                .map(t -> t.getFinishDate().getMonthValue())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> findYearsForIndividualStatistics(String email) {
        Employee employee = employeeService.findByEmail(email);
        return taskRepository.findAllByExecutorId(employee.getId()).stream()
                .filter(t -> t.getFinishDate() != null)
                .map(t -> t.getFinishDate().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> findMonthsForIndividualStatistics(Integer year, String email) {
        Employee employee = employeeService.findByEmail(email);
        return taskRepository.findAllByExecutorId(employee.getId()).stream()
                .filter(t -> t.getFinishDate() != null && t.getFinishDate().getYear() == year)
                .map(t -> t.getFinishDate().getMonthValue())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private IndividualAnalytics getIndividualStats(Employee employee, LocalDate rangeStart, LocalDate rangeEnd) {
        List<Task> tasks = taskRepository.findAllByExecutorIdAndFinishDateBetween(employee.getId(), rangeStart, rangeEnd);
        int delayed = countDelayedTasks(tasks);
        int completedOnTime = tasks.size() - delayed;
        int totalNumberOfTaskOfEmployeeCompleted = tasks.size();
        return IndividualAnalytics.builder()
                .employeeId(employee.getId())
                .employeeFullName(employee.getFullName())
                .employeePosition(employee.getPosition())
                .completedOnTimePercent(calcPercent(completedOnTime, totalNumberOfTaskOfEmployeeCompleted))
                .delayedPercent(calcPercent(delayed, totalNumberOfTaskOfEmployeeCompleted))
                .build();
    }

    private int calcPercent(int number1, int number2) {
        return number2 == 0 ? 0 : (int) Math.round((double) number1 * 100 / number2);
    }

    private int countDelayedTasks(List<Task> tasks) {
        return (int) tasks.stream()
                .filter(task -> task.getFinishDate().isAfter(task.getDeadLine()))
                .count();
    }
}