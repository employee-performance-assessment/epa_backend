package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.questionnaire.QuestionnaireRequestDto;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.util.QuestionnaireStatus;

public interface QuestionnaireService {
    Questionnaire findLastByAuthorAndStatus(String email, QuestionnaireStatus status);

    Questionnaire findLastByAuthorEmail(String email);

    Questionnaire save(QuestionnaireRequestDto questionnaireRequestDto, String email);

    Questionnaire updateLast(QuestionnaireRequestDto questionnaireRequestDto, String email);

    Questionnaire findById(long id);

    Questionnaire updateLastQuestionnaireStatusAndDate(QuestionnaireStatus status, String email);

    Questionnaire duplicateLastShared(String email);

    Questionnaire saveDefaultWithSharedStatus(String email);

    Questionnaire findByEmailAndId(String email, long questionnaireId);
}
