package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.analytics.IndividualAnalyticsResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsFullResponseDto;
import ru.epa.epabackend.dto.analytics.TeamAnalyticsShortResponseDto;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.mapper.EmployeeMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Task;
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
@PropertySource("classpath:analytic.properties")
public class AnalyticsServiceImpl implements AnalyticsService {

    @Value("${percentage_of_tasks_completed_on_time:80}")
    private int percentageOfTasksCompletedOnTime;
    @Value("${percentage_of_tasks_delayed:60}")
    private int percentageOfTasksCompletedDelayed;

    private final TaskRepository taskRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;

    /**
     * Получение командной статистики для админа
     */
    @Override
    @Transactional(readOnly = true)
    public TeamAnalyticsFullResponseDto findTeamStatisticsByAdmin(LocalDate rangeStart, LocalDate rangeEnd,
                                                                  String email) {
        double teamCompletedOnTime = 0;
        double teamDelayed = 0;
        int completedOnTime = 0;
        int delayed = 0;
        List<EmployeeShortResponseDto> leaders = new ArrayList<>();
        List<EmployeeShortResponseDto> deadlineViolators = new ArrayList<>();
        Employee admin = employeeService.findByEmail(email);
        List<Employee> employees = employeeService.findAllByCreatorId(admin.getId());
        for (Employee employee : employees) {
            for (Task task : employee.getTasks()) {
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

        double totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return TeamAnalyticsFullResponseDto.builder()
                    .completedOnTimePercent(
                            getPercentageOfOneNumberFromAnother(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
                    .delayedPercent(getPercentageOfOneNumberFromAnother(teamDelayed, totalNumbersOfTaskOfTeamCompleted))
                    .leaders(leaders)
                    .deadlineViolators(deadlineViolators)
                    .build();
        }
        return new TeamAnalyticsFullResponseDto();
    }

    /**
     * Получение индивидуальной статистики для админа
     */
    @Override
    @Transactional(readOnly = true)
    public List<IndividualAnalyticsResponseDto> findIndividualStatisticsByAdmin(LocalDate rangeStart, LocalDate rangeEnd,
                                                                                String email) {
        List<IndividualAnalyticsResponseDto> employeesShortDto = new ArrayList<>();
        Employee admin = employeeService.findByEmail(email);
        List<Employee> employees = employeeService.findAllByCreatorId(admin.getId());
        for (Employee employee : employees) {
            IndividualAnalyticsResponseDto individualAnalyticsResponseDto =
                    getIndividualDtoOfCompletedTasksWithinSearchPeriod(employee, rangeStart, rangeEnd);
            if (individualAnalyticsResponseDto.getId() != null) {
                employeesShortDto.add(individualAnalyticsResponseDto);
            }
        }
        return employeesShortDto;
    }

    /**
     * Получение командной статистики для сотрудника
     */
    @Override
    @Transactional(readOnly = true)
    public TeamAnalyticsShortResponseDto findTeamStatistics(LocalDate rangeStart, LocalDate rangeEnd, String email) {
        double teamCompletedOnTime = 0;
        double teamDelayed = 0;
        Employee employee = employeeService.findByEmail(email);
        List<Task> tasks = taskRepository.findAllByCreatorIdAndFinishDateBetween(employee.getCreator().getId(),
                rangeStart, rangeEnd);
        for (Task task : tasks) {
            if (task.getFinishDate().isAfter(task.getDeadLine())) {
                teamDelayed++;
            } else {
                teamCompletedOnTime++;
            }
        }

        double totalNumbersOfTaskOfTeamCompleted = teamCompletedOnTime + teamDelayed;
        if (totalNumbersOfTaskOfTeamCompleted != 0) {
            return TeamAnalyticsShortResponseDto.builder()
                    .completedOnTimePercent(
                            getPercentageOfOneNumberFromAnother(teamCompletedOnTime, totalNumbersOfTaskOfTeamCompleted))
                    .delayedPercent(getPercentageOfOneNumberFromAnother(teamDelayed, totalNumbersOfTaskOfTeamCompleted))
                    .build();
        }
        return new TeamAnalyticsShortResponseDto();
    }

    /**
     * Получение индивидуальной статистики для сотрудника
     */
    @Override
    @Transactional(readOnly = true)
    public IndividualAnalyticsResponseDto findIndividualStatistics(LocalDate rangeStart, LocalDate rangeEnd,
                                                                   String email) {
        Employee employee = employeeService.findByEmail(email);
        return getIndividualDtoOfCompletedTasksWithinSearchPeriod(employee, rangeStart, rangeEnd);
    }

    private boolean isFinishedWithinSearchPeriod(Task task, LocalDate rangeStart, LocalDate rangeEnd) {
        return task.getFinishDate() != null &&
                task.getFinishDate().isAfter(rangeStart) &&
                task.getFinishDate().isBefore(rangeEnd);
    }

    private IndividualAnalyticsResponseDto getIndividualDtoOfCompletedTasksWithinSearchPeriod(Employee employee,
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
            return IndividualAnalyticsResponseDto.builder()
                    .id(employee.getId())
                    .fullName(employee.getFullName())
                    .position(employee.getPosition())
                    .completedOnTimePercent(
                            getPercentageOfOneNumberFromAnother(completedOnTime, totalNumberOfTaskOfEmployeeCompleted))
                    .delayedPercent(getPercentageOfOneNumberFromAnother(delayed, totalNumberOfTaskOfEmployeeCompleted))
                    .build();
        }
        return new IndividualAnalyticsResponseDto();
    }

    private int getPercentageOfOneNumberFromAnother(double number1, double number2) {
        return (int) (number1 / number2 * 100);
    }
}