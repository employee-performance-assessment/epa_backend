package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.recommendation.RequestRecommendationDto;
import ru.epa.epabackend.model.Recommendation;

import java.util.List;

/**
 * Интерфейс RecommendationService содержит методы действий с рекомендациями сотрудников.
 *
 * @author Михаил Безуглов
 */
public interface RecommendationService {

    /**
     * Создание рекомендации.
     */
    Recommendation create(RequestRecommendationDto requestRecommendationDto, Long questionnaireId,
                          Long evaluatedId, String senderEmail);

    /**
     * Получение рекомендации по её ID.
     */
    Recommendation findById(Long recommendationId);

    /**
     * Получение списка всех рекомендаций.
     */
    List<Recommendation> findAll();
}
