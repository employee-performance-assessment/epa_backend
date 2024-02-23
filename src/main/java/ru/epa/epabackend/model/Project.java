package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;
import ru.epa.epabackend.util.ProjectStatus;

import java.util.List;
import java.util.Objects;

/**
 * Класс Проект содержит информацию о названии проекта и его статусе,
 * а также содержит список задач проекта.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "projects")
public class Project {

    /**
     * Идентификатор проекта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название проекта.
     */
    private String name;

    /**
     * Статус проекта
     * Возможные статусы проекта:  ABANDONED, TODO, WIP, COMPLETED, DISTRIBUTED.
     */
    private ProjectStatus status;

    /**
     * Список задач проекта.
     */
    @ManyToMany
    @JoinTable(
            name = "projects_tasks",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private List<Task> tasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id) && name.equals(project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", tasks=" + tasks +
                '}';
    }
}
