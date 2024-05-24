package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Класс Рекомендации содержит информацию о том кто оценивает
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
@Table(name = "recommendations")
public class Recommendation {

    /**
     * Идентификатор рекомендации
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Сотрудник, получивший рекомендацию
     */
    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private Employee recipient;

    /**
     * Руководитель, отправивший рекомендацию
     */
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Employee sender;

    /**
     * Дата создания рекомендации
     */
    @Column(name = "create_day")
    private LocalDate createDay;

    /**
     * Описание рекомендации
     */
    @JoinColumn(name = "recommendation")
    private String recommendation;

    /**
     * Анкета по которой оцениваем
     */
    @ManyToOne
    @JoinColumn(name = "questionnaire_id", referencedColumnName = "id")
    private Questionnaire questionnaire;
}
