package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    Recommendation findByRecipientEmailAndQuestionnaireId(String email, Long questionnaireId);

    Recommendation getByRecipientIdAndQuestionnaireId(Long evaluatedId, Long questionnaireId);
}
