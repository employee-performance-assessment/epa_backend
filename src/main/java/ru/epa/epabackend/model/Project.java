package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import ru.epa.epabackend.util.ProjectStatus;

import java.util.List;

/**
 * Класс Проект содержит информацию о названии проекта и его статусе,
 * а также содержит список задач проекта.
 *
 * @author Михаил Безуглов и Константин Осипов
 */
@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(of = {"id", "name"})
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
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    /**
     * Список задач проекта.
     */
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private List<Task> tasks;

    /**
     * Список сотрудников проекта.
     */
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "projects_employees",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "employee_id")})
    private List<Employee> employees;
}
