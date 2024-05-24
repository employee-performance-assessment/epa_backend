package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Класс Оценки Сотрудника содержит информацию о том кто оценивает
 * и кого оценивают, а также дату оценки, название оценки и анкету по которой оцениваем.
 *
 * @author Михаил Безуглов
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employee_evaluation")
public class EmployeeEvaluation {

    /**
     * Идентификатор оценки сотрудника
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Сотрудник, которого оцениваем
     */
    @ManyToOne
    @JoinColumn(name = "evaluated_id", referencedColumnName = "id")
    private Employee evaluated;

    /**
     * Сотрудник, который оценивает
     */
    @ManyToOne
    @JoinColumn(name = "evaluator_id", referencedColumnName = "id")
    private Employee evaluator;

    /**
     * Дата оценки
     */
    @Column(name = "create_day")
    private LocalDate createDay = LocalDate.now();

    /**
     * Название оценки
     */
    @ManyToOne
    @JoinColumn(name = "criteria_id", referencedColumnName = "id")
    private Criteria criteria;

    /**
     * Количество звезд
     */
    @JoinColumn(name = "score")
    private Integer score;

    /**
     * Анкета по которой оцениваем
     */
    @ManyToOne
    @JoinColumn(name = "questionnaire_id", referencedColumnName = "id")
    private Questionnaire questionnaire;
}
