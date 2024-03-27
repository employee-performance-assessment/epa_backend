package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.mapper.EmployeeMapper;
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
import java.util.Objects;

/**
 * Класс AnalyticServiceImpl содержит методы для аналитики задач и оценок.
 *
 * @author Владислав Осипов
 */
@Service
@RequiredArgsConstructor
@Transactional
@PropertySource("classpath:application.yml")
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TaskRepository taskRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;
    @Value("${percentage_of_tasks_completed_on_time:80}")
    private int percentageOfTasksCompletedOnTime;
    @Value("${percentage_of_tasks_delayed:60}")
    private int percentageOfTasksCompletedDelayed;

    /**
     * Получение командной статистики для админа
     */
    @Override
    @Transactional(readOnly = true)
    public TeamAnalytics getTeamStatisticsByAdmin(LocalDate rangeStart, LocalDate rangeEnd,
                                                  String email) {
        int teamCompletedOnTime = 0;
        int teamDelayed = 0;
        int completedOnTime = 0;
        int delayed = 0;
        List<EmployeeShortResponseDto> leaders = new ArrayList<>();
        List<EmployeeShortResponseDto> deadlineViolators = new ArrayList<>();
        Employee admin = employeeService.findByEmail(email);
        List<Employee> employees = employeeService.findAllByCreatorIdShort(admin.getId());
        for (Employee employee : employees) {
            for (Task task : taskRepository
                    .findAllByExecutorIdAndFinishDateBetween(employee.getId(), rangeStart, rangeEnd)) {
                if (isFinishedWithinSearchPeriod(task, rangeStart, rangeEnd)) {
                    if (task.getFinishDate().isAfter(task.getDeadLine())) {
                        delayed++;
                    } else {
                        completedOnTime++;
                    }
                }
            }
            if (!employee.getTasks().isEmpty()) {
                if ((completedOnTime / employee.getTasks().size()) * 100 >
                        percentageOfTasksCompletedOnTime) {
                    leaders.add(employeeMapper.mapToShortDto(employee));
                }
                if (delayed != 0 && (delayed / employee.getTasks().size()) * 100 >
                        percentageOfTasksCompletedDelayed) {
                    deadlineViolators.add(employeeMapper.mapToShortDto(employee));
                }
                teamCompletedOnTime += completedOnTime;
                completedOnTime = 0;
                teamDelayed += delayed;
                delayed = 0;
            }
        }

        int totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return TeamAnalytics.builder()
                    .completedOnTimePercent(
                            calcPercent(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
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
    public List<IndividualAnalytics> getIndividualStatisticsByAdmin(LocalDate rangeStart, LocalDate rangeEnd,
                                                                    String email) {
        List<IndividualAnalytics> employeesShortDto = new ArrayList<>();
        Employee admin = employeeService.findByEmail(email);
        List<Employee> employees = employeeService.findAllByCreatorIdShort(admin.getId());
        for (Employee employee : employees) {
            IndividualAnalytics individualAnalytics =
                    getIndividualDtoOfCompletedTasksWithinSearchPeriod(employee, rangeStart, rangeEnd);
            if (individualAnalytics != null) {
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
    public TeamAnalytics getTeamStatistics(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        int teamCompletedOnTime = 0;
        int teamDelayed = 0;
        Employee employee = employeeService.findByEmail(email);
        List<Task> tasks = taskRepository.findAllByOwnerIdAndFinishDateBetween(employee.getCreator().getId(),
                rangeStart, rangeEnd);
        for (Task task : tasks) {
            if (task.getFinishDate().isAfter(task.getDeadLine())) {
                teamDelayed++;
            } else {
                teamCompletedOnTime++;
            }
        }

        int totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return TeamAnalytics.builder()
                    .completedOnTimePercent(
                            calcPercent(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
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
    public IndividualAnalytics getIndividualStatistics(LocalDate rangeStart, LocalDate rangeEnd,
                                                       String email) {
        Employee employee = employeeService.findByEmail(email);
        IndividualAnalytics individualAnalytics =
                getIndividualDtoOfCompletedTasksWithinSearchPeriod(employee, rangeStart, rangeEnd);
        return Objects.requireNonNullElseGet(individualAnalytics, IndividualAnalytics::new);
    }

    private boolean isFinishedWithinSearchPeriod(Task task, LocalDate rangeStart, LocalDate rangeEnd) {
        return task.getFinishDate() != null &&
                task.getFinishDate().isAfter(rangeStart) &&
                task.getFinishDate().isBefore(rangeEnd);
    }

    private IndividualAnalytics getIndividualDtoOfCompletedTasksWithinSearchPeriod(Employee employee,
                                                                                   LocalDate rangeStart,
                                                                                   LocalDate rangeEnd) {
        int completedOnTime = 0;
        int delayed = 0;
        for (Task task : employee.getTasks()) {
            if (isFinishedWithinSearchPeriod(task, rangeStart, rangeEnd)) {
                if (task.getFinishDate().isAfter(task.getDeadLine())) {
                    delayed++;
                } else {
                    completedOnTime++;
                }
            }
        }

        int totalNumberOfTaskOfEmployeeCompleted = completedOnTime + delayed;
        if (totalNumberOfTaskOfEmployeeCompleted != 0) {
            return IndividualAnalytics.builder()
                    .employeeId(employee.getId())
                    .employeeFullName(employee.getFullName())
                    .employeePosition(employee.getPosition())
                    .completedOnTimePercent(
                            calcPercent(completedOnTime, totalNumberOfTaskOfEmployeeCompleted))
                    .delayedPercent(calcPercent(delayed, totalNumberOfTaskOfEmployeeCompleted))
                    .build();
        }
        return null;
    }

    private int calcPercent(int number1, int number2) {
        return number1 * 100 / number2;
    }
}