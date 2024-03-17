package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс Оценка содержит информацию о названии оценки.
 *
 * @author Михаил Безуглов
 */
@Entity
@Table(name = "evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"id", "name"})
public class Evaluation {

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
