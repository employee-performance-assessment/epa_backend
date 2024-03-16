package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.epa.epabackend.util.DateConstant;

import java.time.LocalDate;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс TaskRequestDto для передачи тела запроса на сервер с данными для создания и обновления задачи
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
    @Size(min = 2, max = 250, groups = {Create.class, Update.class})
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯЁё0-9\\s\\-\\.\\,]+$")
    private String name;

    /**
     * Описание задачи.
     */
    @NotBlank(groups = {Create.class})
    @Size(min = 2, max = 250, groups = {Create.class, Update.class})
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯЁё0-9\\s\\-\\.\\,]+$")
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
    @Range(min = 0, max = 99999, groups = {Create.class, Update.class})
    private Integer basicPoints;

    /**
     * Дополнительные баллы, которые вычитаются или прибавляются, в зависимости от того
     * выполнил ли в срок задачу исполнитель. Задаются руководителем.
     */
    @Positive(groups = {Create.class, Update.class})
    @Range(min = 0, max = 99999, groups = {Create.class, Update.class})
    private Integer penaltyPoints;
}