package ru.epa.epabackend.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.epa.epabackend.util.DateConstant;

import java.time.LocalDate;

import static ru.epa.epabackend.util.StringPatterns.CYRILLIC_LATIN_ALPHABET_AND_NUMBERS;
import static ru.epa.epabackend.util.StringPatterns.CYRILLIC_LATIN_NUMBERS_SPECIAL_CHARACTERS;
import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс для передачи тела запроса на сервер с данными для создания и обновления задачи
 *
 * @author Владислав Осипов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestTaskDto {

    /**
     * Название задачи
     */
    @NotBlank(groups = {Create.class}, message = "Название не должно быть пустым")
    @Size(min = 1, max = 255, groups = {Create.class, Update.class},
            message = "Допустимая длина названия задачи от 1 до 255 символов")
    @Pattern(regexp = CYRILLIC_LATIN_ALPHABET_AND_NUMBERS, groups = {Create.class, Update.class},
    message = "В названии задачи разрешены русские, английские символы, цифры, пробел, тире, точка и запятая")
    private String name;

    /**
     * Описание задачи
     */
    @NotBlank(groups = {Create.class}, message = "Описание задачи не должно быть пустым")
    @Size(min = 1, max = 255, groups = {Create.class, Update.class},
            message = "Допустимая длина описания задачи от 1 до 255 символов")
    @Pattern(regexp = CYRILLIC_LATIN_NUMBERS_SPECIAL_CHARACTERS, groups = {Create.class, Update.class},
            message = "В описании задачи разрешены русские, английские символы, цифры, пробел и спецсимволы " +
                    ",:;?!*+%-<>@[]/_{}$#")
    private String description;

    /**
     * ID проекта
     */
    @NotNull(groups = {Create.class}, message = "Необходимо указать проект")
    @Positive(groups = {Create.class, Update.class}, message = "id проекта должен быть положительным числом")
    private Long projectId;

    /**
     * ID Сотрудника, выполняющего задачу
     */
    @Positive(groups = {Create.class, Update.class}, message = "id исполнителя должен быть положительным числом")
    private Long executorId;

    /**
     * Дата до которой должна выполниться задача
     */
    @NotNull(groups = {Create.class}, message = "Необходимо указать дедлайн")
    @FutureOrPresent(groups = {Create.class, Update.class}, message = "Невозможно указать дедлайн в прошлом")
    @JsonFormat(pattern = DateConstant.DATE_PATTERN)
    private LocalDate deadLine;

    /**
     * Статус выполнения задачи
     * Возможные статусы: NEW, IN_PROGRESS, REVIEW, DONE, CANCELED
     */
    private String status;

    /**
     * Сложность задачи измеряемая в баллах, задается руководителем
     */
    @NotNull(groups = {Create.class}, message = "Необходимо указать баллы за задачу")
    @Positive(groups = {Create.class, Update.class}, message = "Баллы за задачу измеряются положительным числом")
    @Range(min = 0, max = 99999, groups = {Create.class, Update.class}, message = "Допустимый диапазон баллов за " +
            "задачу от 0 до 99999")
    private Integer basicPoints;

    /**
     * Дополнительные баллы, которые вычитаются или прибавляются, в зависимости от того
     * выполнил ли в срок задачу исполнитель. Задаются руководителем.
     */
    @NotNull(groups = {Create.class}, message = "Необходимо указать бонусные и штрафные баллы")
    @Positive(groups = {Create.class, Update.class}, message = "Бонусные и штрафные баллы за задачу измеряются " +
            "положительным числом")
    @Range(min = 0, max = 99999, groups = {Create.class, Update.class}, message = "Допустимый диапазон бонусных " +
            "и штрафных баллов за задачу от 0 до 99999")
    private Integer penaltyPoints;
}