package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.epa.epabackend.util.DateConstant;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;

/**
 * Класс TaskShortDto содержит краткую структуру данных, которая используется для передачи
 * информации между различными слоями приложения.
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskShortDto {
    /**
     * ID задачи.
     */
    private Long id;

    /**
     * Название задачи.
     */
    private String name;

    /**
     * Дата до которой должна выполниться задача..
     */
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate deadLine;

    /**
     * Статус выполнения задачи
     * Возможные статусы: NEW, IN_PROGRESS, REVIEW, DONE, CANCELED.
     */
    private TaskStatus status;

    /**
     * Сложность задачи измеряемая в баллах, задается руководителем.
     */
    private Integer basicPoints;
}
