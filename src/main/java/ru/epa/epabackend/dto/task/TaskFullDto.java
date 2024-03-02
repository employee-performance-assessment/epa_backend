package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.epa.epabackend.dto.employee.EmployeeDtoResponseShort;
import ru.epa.epabackend.dto.project.ProjectOutDtoShort;
import ru.epa.epabackend.util.DateConstant;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;

/**
 * Класс TaskOutDto содержит структуру данных, которая используется для передачи
 * информации между различными слоями приложения.
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskFullDto {

    /**
     * ID задачи.
     */
    private Long id;

    /**
     * Название задачи.
     */
    private String name;

    /**
     * Описание задачи.
     */
    private String description;

    /**
     * Описание проекта.
     */
    private ProjectOutDtoShort project;

    /**
     * Сотрудник выполняющий задачу.
     */
    private EmployeeDtoResponseShort executor;

    /**
     * Дата взятие задачи в работу.
     */
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate startDate;

    /**
     * Дата до которой должна выполниться задача..
     */
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate deadLine;

    /**
     * Дата выполнения задачи.
     */
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate finishDate;

    /**
     * Статус выполнения задачи
     * Возможные статусы: NEW, IN_PROGRESS, REVIEW, DONE, CANCELED.
     */
    private TaskStatus status;

    /**
     * Сложность задачи измеряемая в баллах, задается руководителем.
     */
    private Integer basicPoints;

    /**
     * Количество баллов за выполнение задачи с учетом времени выполнения,
     * если задача выполнена раньше заложенного времени то количество баллов увеличивается,
     * если задача выполнена после дедлайна количество баллов уменьшается.
     */
    private Integer points;

    /**
     * Дополнительные баллы, которые вычитаются или прибавляются, в зависимости от того
     * выполнил ли в срок задачу исполнитель. Задаются руководителем.
     */
    private Integer penaltyPoints;
}