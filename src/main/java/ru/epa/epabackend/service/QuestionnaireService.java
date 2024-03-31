package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.questionnaire.QuestionnaireRequestDto;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.util.List;

public interface QuestionnaireService {
    Questionnaire findLastByAuthorAndStatus(String email, QuestionnaireStatus status);

    Questionnaire findLastByAuthorEmail(String email);

    Questionnaire saveWithParameters(QuestionnaireStatus status, Employee author, List<Criteria> criterias);

    Questionnaire updateLast(QuestionnaireRequestDto questionnaireRequestDto, String email);

    Questionnaire findById(long id);

    Questionnaire sendQuestionnaireToEmployees(String email);

    Questionnaire findByEmailAndId(String email, long questionnaireId);

    List<Questionnaire> findAllByAuthorIdAndStatus(String email, QuestionnaireStatus status);
}
