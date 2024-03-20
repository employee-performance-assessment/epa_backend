package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.epa.epabackend.dto.employee.EmployeeShortResponseDto;
import ru.epa.epabackend.dto.project.ProjectShortResponseDto;
import ru.epa.epabackend.util.DateConstant;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;

/**
 * Класс TaskFullResponseDto для передачи полной информации о задаче
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskFullResponseDto {

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
    private ProjectShortResponseDto project;

    /**
     * Сотрудник выполняющий задачу.
     */
    private EmployeeShortResponseDto executor;

    /**
     * Дата взятие задачи в работу.
     */
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate startDate;

    /**
     * Дата до которой должна выполниться задача.
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