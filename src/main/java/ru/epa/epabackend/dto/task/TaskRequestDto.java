package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.epa.epabackend.util.DateConstant;

import java.time.LocalDate;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

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
    @NotBlank(groups = {Create.class})
    @Size(min = 2, max = 255, groups = {Create.class, Update.class})
    private String name;

    /**
     * Описание задачи.
     */
    @NotBlank(groups = {Create.class})
    @Size(min = 2, max = 255, groups = {Create.class, Update.class})
    private String description;

    /**
     * ID проекта.
     */
    @NotNull(groups = {Create.class})
    @Positive(groups = {Create.class, Update.class})
    private Long projectId;

    /**
     * ID Сотрудника, выполняющего задачу.
     */
    @Positive(groups = {Create.class, Update.class})
    private Long executorId;

    /**
     * Дата до которой должна выполниться задача..
     */
    @NotNull(groups = {Create.class})
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
    @Positive(groups = {Create.class, Update.class})
    private Integer basicPoints;

    /**
     * Дополнительные баллы, которые вычитаются или прибавляются, в зависимости от того
     * выполнил ли в срок задачу исполнитель. Задаются руководителем.
     */
    @Positive(groups = {Create.class, Update.class})
    private Integer penaltyPoints;
}