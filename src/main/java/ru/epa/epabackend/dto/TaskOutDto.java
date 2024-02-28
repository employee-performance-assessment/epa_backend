package ru.epa.epabackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.util.Constants;
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
public class TaskOutDto {

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
    private Project project;

    /**
     * Руководитель создавший задачу и контролирующий выполнение задачи.
     */
    private Employee creator;

    /**
     * Сотрудник выполняющий задачу.
     */
    private Employee executor;

    /**
     * Дата взятие задачи в работу.
     */
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
    private LocalDate startDate;

    /**
     * Дата выполнения задачи.
     */
    @JsonFormat(pattern = Constants.DATE_TIME_STRING)
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