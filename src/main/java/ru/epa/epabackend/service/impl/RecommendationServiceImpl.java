package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.recommendation.RecommendationRequestDto;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.repository.RecommendationRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.RecommendationService;

import java.time.LocalDate;
import java.util.List;

/**
 * Класс RecommendationServiceImpl содержит бизнес-логику работы с рекомендациями.
 *
 * @author Михаил Безуглов
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final EmployeeService employeeService;

    /**
     * Сохранение рекомендации.
     */
    @Override
    public Recommendation create(RecommendationRequestDto recommendationRequestDto,
                                 Long recipientId, Long senderId) {
        Employee recipient = employeeService.findById(recipientId);
        Employee sender = employeeService.findById(senderId);
        Recommendation recommendation = recommendationMapper.mapToEntity(recommendationRequestDto,
                recipient, sender);
        recommendation.setCreateDay(LocalDate.now());
        return recommendationRepository.save(recommendation);
    }

    /**
     * Получение рекомендации по ID.
     */
    @Override
    public Recommendation findById(Long recommendationId) {
        return recommendationRepository.findById(recommendationId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Рекомендация с id %s не найдена",
                        recommendationId)));
    }

    /**
     * Получение списка рекомендаций для сотрудника с ID.
     */
    @Override
    public List<Recommendation> findAllByRecipientId(Long recipientId) {
        return recommendationRepository.findAllByRecipientId(recipientId);
    }

    /**
     * Получение всех рекомендаций.
     */
    @Override
    public List<Recommendation> findAll() {
        return recommendationRepository.findAll();
    }
}
