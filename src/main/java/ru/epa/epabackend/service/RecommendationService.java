package ru.epa.epabackend.service;

import ru.epa.epabackend.model.Recommendation;

/**
 * Интерфейс RecommendationService содержит методы действий с рекомендациями сотрудников.
 *
 * @author Михаил Безуглов
 */
public interface RecommendationService {

    /**
     * Создание рекомендации.
     */
    Recommendation create(String recommendation, Long questionnaireId, Long evaluatedId, String senderEmail);

    /**
     * Получение рекомендации по id получателя и id анкеты
     */
    Recommendation getByRecipientIdAndQuestionnaireId(Long evaluatedId, Long questionnaireId);
}
