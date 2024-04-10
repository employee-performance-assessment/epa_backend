package ru.epa.epabackend.analytics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.IndividualAnalytics;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.model.TeamAnalytics;
import ru.epa.epabackend.repository.TaskRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.impl.AnalyticsServiceImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalyticsUnitTests {
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final String email1 = "qwerty1@gmail.com";
    private static final String email2 = "qwerty2@gmail.com";
    private final LocalDate createDate = LocalDate.now().minusDays(4);
    private final LocalDate startDate = LocalDate.now().minusDays(3);
    private final LocalDate deadLineDate = LocalDate.now().minusDays(3);
    private final LocalDate finishDate = LocalDate.now().minusDays(4);
    private final LocalDate rangeStart = LocalDate.now().minusDays(2);
    private final LocalDate rangeEnd = LocalDate.now().plusDays(2);
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private EmployeeService employeeService;
    @InjectMocks
    private AnalyticsServiceImpl analyticsService;
    private Employee admin;
    private Employee employee;
    private Employee employee2;
    private Task task;
    private Task task2;
    private IndividualAnalytics individualAnalytics;
    private TeamAnalytics teamAnalytics;


    @BeforeEach
    public void unit() {
        employee = Employee.builder()
                .id(ID_1)
                .email(email1)
                .build();
        employee2 = Employee.builder()
                .id(ID_2)
                .email(email2)
                .creator(employee)
                .build();
        task = Task.builder()
                .id(ID_1)
                .name("task1")
                .executor(employee)
                .createDate(createDate)
                .startDate(startDate)
                .deadLine(deadLineDate)
                .finishDate(finishDate)
                .build();
        task2 = Task.builder()
                .id(ID_2)
                .name("task2")
                .executor(employee)
                .createDate(createDate)
                .startDate(startDate)
                .deadLine(finishDate)
                .finishDate(finishDate)
                .build();
        individualAnalytics = IndividualAnalytics.builder()
                .employeeId(ID_2)
                .build();
        List<Employee> leaders = new ArrayList<>();
        leaders.add(employee2);
        teamAnalytics = TeamAnalytics.builder()
                .completedOnTimePercent(10)
                .delayedPercent(10)
                .leaders(leaders)
                .build();
    }

    @Test
    @DisplayName("Получение командной статистики для админа")
    void shouldGetTeamStatsByAdminWhenTasksNotEmpty() {
        List<Employee> leaders = new ArrayList<>();
        List<Employee> deadlineViolators = new ArrayList<>();
        List<Task> allTasks = new ArrayList<>();
        allTasks.add(task);
        Map<Employee, List<Task>> employeeTasks = new HashMap<>();
        employeeTasks.put(employee, allTasks);
        leaders.add(employee);
        deadlineViolators.add(employee2);

        when(taskRepository.findAllByOwnerEmailAndFinishDateBetween(email1, rangeStart, rangeEnd))
                .thenReturn(allTasks);

        TeamAnalytics teamAnalyticsResult = analyticsService.getTeamStatsByAdmin(rangeStart, rangeEnd, email1);
        List<Employee> expectedAnalytics = new ArrayList<>();
        expectedAnalytics.add(employee);
        assertNotNull(teamAnalyticsResult);
        assertEquals(expectedAnalytics, teamAnalyticsResult.getLeaders());
    }

    @Test
    @DisplayName("Получение индивидуальной статистики для админа")
    void shouldGetIndividualStatsByAdmin() {
        List<IndividualAnalytics> employeesShortDto = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        employees.add(employee2);
        when(employeeService.findAllByCreatorEmail(email2)).thenReturn(List.of(employee2));
        employeesShortDto.add(individualAnalytics);
        List<IndividualAnalytics> individualAnalyticsResult = analyticsService
                .getIndividualStatsByAdmin(rangeStart, rangeEnd, email2);
        int expectedAnalyticsSize = 1;
        assertNotNull(individualAnalyticsResult);
        assertEquals(expectedAnalyticsSize, individualAnalyticsResult.size());
    }

    @Test
    @DisplayName("Получение командной статистики для сотрудника")
    void shouldGetTeamStats() {
        when(employeeService.findByEmail(email2)).thenReturn(employee2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task2);
        employee.setTasks(Set.of(task2));
        when(taskRepository.findAllByOwnerIdAndFinishDateBetween(ID_1, rangeStart, rangeEnd)).thenReturn(tasks);
        TeamAnalytics teamAnalyticsResult = analyticsService.getTeamStats(rangeStart, rangeEnd, email2);
        int expectedPercent = 100;
        assertEquals(expectedPercent, teamAnalyticsResult.getCompletedOnTimePercent());
    }

    @Test
    @DisplayName("Получение индивидуальной статистики для сотрудника")
    void shouldGetIndividualStats() {
        when(employeeService.findByEmail(email2)).thenReturn(employee2);
        IndividualAnalytics individualAnalyticsResult = analyticsService.getIndividualStats(rangeStart, rangeEnd, email2);
        int expectId = 2;
        assertNotNull(individualAnalyticsResult);
        assertEquals(expectId, individualAnalyticsResult.getEmployeeId());
    }
}
