package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Recommendation;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    //List<Recommendation> findAllByRecipientEmail(String recipientEmail);

    Recommendation findByRecipientEmailAndQuestionnaireId(String email, Long questionnaireId);

    Recommendation findByRecipientIdAndQuestionnaireId(Long recipientId, Long questionnaireId);
}
