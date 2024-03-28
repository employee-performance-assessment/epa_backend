package ru.epa.epabackend.service;

import ru.epa.epabackend.dto.recommendation.RecommendationRequestDto;
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
    Recommendation create(RecommendationRequestDto recommendationRequestDto,
                          Long recipientId, Long senderId);

    /**
     * Получение рекомендации по её ID.
     */
    Recommendation findById(Long recommendationId);

    /**
     * Получение списка рекомендаций по ID сотрудника.
     */
    List<Recommendation> findAllByRecipientId(Long recipientId);

    /**
     * Получение списка всех рекомендаций.
     */
    List<Recommendation> findAll();
}
