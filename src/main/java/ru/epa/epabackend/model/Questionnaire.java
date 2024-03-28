package ru.epa.epabackend.model;

import jakarta.persistence.*;
import lombok.*;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Сущность Questionnaire представляет собой анкету, которую админ создаёт, редактирует и отправляет на проставление
 * оценок всем своим сотрудникам включая себя
 */
@Entity
@Table(name = "questionnaires")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = {"id"})
public class Questionnaire {
    /**
     * Идентификатор анкеты
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Администратор, создавший анкету
     */
    @OneToOne
    @JoinColumn(name = "author_id")
    private Employee author;

    /**
     * Дата создания анкеты
     */
    private LocalDate created;

    /**
     * Список критериев в анкете
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "questionnaires_criterias",
            joinColumns = @JoinColumn(name = "questionnaire_id"),
            inverseJoinColumns = @JoinColumn(name = "criteria_id"))
    @Builder.Default
    private Set<Criteria> criterias = new LinkedHashSet<>();

    /**
     * Статус анкеты (CREATED, SHARED)
     */
    @Enumerated(EnumType.STRING)
    private QuestionnaireStatus status;
}