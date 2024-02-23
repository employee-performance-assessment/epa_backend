package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * Класс Технологии содержит название инструментов, применяющихся при работе в проектах
 * и включающий языки программирования, фреймворки, системы управления базами данных, компиляторы и т.д.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "technologies")
public class Technology {

    /**
     * Идентификатор технологии.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название технологии.
     */
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technology that = (Technology) o;
        return id.equals(that.id) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Technology{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
