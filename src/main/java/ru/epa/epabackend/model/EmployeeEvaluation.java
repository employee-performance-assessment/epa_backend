package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Класс Оценки Сотрудника содержит информацию о том кто оценивает
 * и кого оценивают, а также дату оценки и название оценки.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee_evaluations")
public class EmployeeEvaluation {

    /**
     * Идентификатор оценки сотрудника.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Сотрудник, которого оцениваем.
     */
    @ManyToOne
    @JoinColumn(name = "appraiser_id")
    private Employee appraiser;

    /**
     * Сотрудник, который оценивает.
     */
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    /**
     * Дата оценки.
     */
    @Column(name = "day_create")
    private LocalDate dayCreate;

    /**
     * Название оценки.
     */
    @ManyToOne
    @JoinColumn(name = "evaluation_id", referencedColumnName = "id")
    private Evaluation evaluation;
}
