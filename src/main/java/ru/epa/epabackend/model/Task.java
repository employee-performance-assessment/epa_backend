package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс Задача содержит информацию о названии, описании, создателе, исполнителе,
 * времени заложенному на выполнение этой задачи, сложности задачи (сложность измеряется в балах,
 * чем больше баллов тем сложнее задача),
 * дату начала и завершения выполнения задачи сотрудником, статусе задачи, количестве баллов
 * начисленных сотруднику за выполнение задачи и дополнительном количестве баллов за задачу.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {

    /**
     * Идентификатор проекта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название задачи
     */
    private String name;

    /**
     * Описание задачи
     */
    private String description;

    /**
     * Описание проекта
     */
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    /**
     * Сотрудник выполняющий задачу
     */

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private Employee executor;

    /**
     * Дата создания задачи
     */
    @Column(name = "create_date")
    private LocalDate createDate;

    /**
     * Дата взятие задачи в работу
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * Дата до которой должна выполниться задача
     */

    @Column(name = "deadLine")
    private LocalDate deadLine;

    /**
     * Дата выполнения задачи
     */
    @Column(name = "finish_date")
    private LocalDate finishDate;

    /**
     * Статус выполнения задачи
     * Возможные статусы: NEW, IN_PROGRESS, REVIEW, DONE, CANCELED
     */
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    /**
     * Сложность задачи измеряемая в баллах, задается руководителем
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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Employee owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) && name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", status=" + status +
                ", basicPoints=" + basicPoints +
                ", points=" + points +
                ", penaltyPoints=" + penaltyPoints +
                '}';
    }
}