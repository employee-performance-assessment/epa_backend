package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.epa.epabackend.dto.project.ResponseProjectShortDto;
import ru.epa.epabackend.util.DateConstant;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;

/**
 * Класс для передачи сокращенной информации о задаче, предназначенной для списков
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseTaskShortDto {
    /**
     * ID задачи
     */
    private Long id;

    /**
     * Название задачи
     */
    private String name;

    /**
     * Описание проекта
     */
    private ResponseProjectShortDto project;

    /**
     * Дата создания задачи
     */
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate createDate;

    /**
     * Дата до которой должна выполниться задача
     */
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate deadLine;

    /**
     * Статус выполнения задачи
     * Возможные статусы: NEW, IN_PROGRESS, REVIEW, DONE, CANCELED
     */
    private TaskStatus status;

    /**
     * Сложность задачи измеряемая в баллах, задается руководителем
     */
    private Integer basicPoints;

    /**
     * Дополнительные баллы, которые вычитаются или прибавляются, в зависимости от того
     * выполнил ли в срок задачу исполнитель. Задаются руководителем
     */
    private Integer penaltyPoints;
}