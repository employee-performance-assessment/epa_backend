package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.util.List;
import java.util.Optional;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    Optional<Questionnaire> findLastByAuthorEmailAndStatusOrderByIdAsc(String authorEmail, QuestionnaireStatus status);

    Optional<Questionnaire> findLastByAuthorEmailOrderByIdAsc(String email);

    List<Questionnaire> findAllByAuthorIdAndStatus(Long authorId, QuestionnaireStatus shared);
}
