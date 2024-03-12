package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.epa.epabackend.util.DateConstant;

import java.time.LocalDate;

/**
 * Класс TaskInDto содержит структуру данных, которая используется для передачи
 * информации между различными слоями приложения.
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDto {

    /**
     * Название задачи.
     */
    @NotBlank
    private String name;

    /**
     * Описание задачи.
     */
    @NotBlank
    private String description;

    /**
     * ID проекта.
     */
    @NotNull
    private Long projectId;

    /**
     * ID Сотрудника, выполняющего задачу.
     */
    private Long executorId;

    /**
     * Дата до которой должна выполниться задача..
     */
    @NotNull
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate deadLine;

    /**
     * Статус выполнения задачи
     * Возможные статусы: NEW, IN_PROGRESS, REVIEW, DONE, CANCELED.
     */
    private String status;

    /**
     * Сложность задачи измеряемая в баллах, задается руководителем.
     */
    @Positive
    private Integer basicPoints;

    /**
     * Дополнительные баллы, которые вычитаются или прибавляются, в зависимости от того
     * выполнил ли в срок задачу исполнитель. Задаются руководителем.
     */
    @Positive
    private Integer penaltyPoints;
}