package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.recommendation.RequestRecommendationDto;
import ru.epa.epabackend.mapper.RecommendationMapper;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.model.Recommendation;
import ru.epa.epabackend.repository.RecommendationRepository;
import ru.epa.epabackend.service.EmployeeService;
import ru.epa.epabackend.service.QuestionnaireService;
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
    private final QuestionnaireService questionnaireService;

    /**
     * Сохранение рекомендации.
     */
    @Override
    public Recommendation create(RequestRecommendationDto requestRecommendationDto, Long questionnaireId,
                                 String recipientEmail, String senderEmail) {
        Employee recipient = employeeService.findByEmail(recipientEmail);
        Employee sender = employeeService.findByEmail(senderEmail);
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        Recommendation recommendation = recommendationMapper.mapToEntity(requestRecommendationDto, questionnaire,
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
        return recommendationRepository.findAllByRecipientEmail(recipientEmail);
    }

    /**
     * Получение всех рекомендаций.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Recommendation> findAll() {
        return recommendationRepository.findAll();
    }
}
