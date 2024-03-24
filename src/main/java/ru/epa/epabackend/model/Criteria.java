package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс Оценка содержит информацию о названии оценки.
 *
 * @author Михаил Безуглов
 */
@Entity
@Table(name = "criteria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"id", "name"})
public class Criteria {

    /**
     * Идентификатор оценки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название оценки.
     */
    private String name;
}
