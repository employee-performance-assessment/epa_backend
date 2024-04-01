package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.recommendation.RequestRecommendationDto;
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
@Slf4j
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
    public Recommendation create(RequestRecommendationDto requestRecommendationDto,
                                 String recipientEmail, String senderEmail) {
        log.info("Сохранение рекомендации {}", requestRecommendationDto.getRecommendation());
        Employee recipient = employeeService.findByEmail(recipientEmail);
        Employee sender = employeeService.findByEmail(senderEmail);
        Recommendation recommendation = recommendationMapper.mapToEntity(requestRecommendationDto,
                recipient, sender);
        recommendation.setCreateDay(LocalDate.now());
        return recommendationRepository.save(recommendation);
    }

    /**
     * Получение рекомендации по ID.
     */
    @Override
    @Transactional(readOnly = true)
    public Recommendation findById(Long recommendationId) {
        log.info("Получение рекомендации идентификатору {}", recommendationId);
        return recommendationRepository.findById(recommendationId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Рекомендация с id %s не найдена",
                        recommendationId)));
    }

    /**
     * Получение списка рекомендаций для сотрудника с ID.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Recommendation> findAllByRecipientEmail(String recipientEmail) {
        log.info("Получение списка рекомендаций для сотрудника с идентификатором {}", recipientEmail);
        return recommendationRepository.findAllByRecipientEmail(recipientEmail);
    }

    /**
     * Получение всех рекомендаций.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Recommendation> findAll() {
        log.info("Получение всех рекомендаций");
        return recommendationRepository.findAll();
    }
}
