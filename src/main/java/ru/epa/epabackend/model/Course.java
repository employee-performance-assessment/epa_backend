package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;


/**
 * Класс Курс содержит информацию о названии, дате начала и окончания, стеке курса,
 * а также ссылку на курс если его можно пройти онлайн.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "courses")
public class Course {

    /**
     * Идентификатор курса.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название курса.
     */
    private String name;

    /**
     * Ссылка на курс.
     */
    private String link;

    /**
     * Дата начала курса.
     */
    private LocalDate startDate;

    /**
     * Дата окончания курса.
     */
    private LocalDate finishDate;

    /**
     * Поле стек технологий курса.
     */
    @ManyToMany
    @JoinTable(
            name = "courses_technologies",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "technologies_id"))
    private Set<Technology> technologies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id.equals(course.id) && name.equals(course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", technologies=" + technologies +
                '}';
    }
}
