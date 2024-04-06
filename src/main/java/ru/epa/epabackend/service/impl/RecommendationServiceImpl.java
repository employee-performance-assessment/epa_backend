package ru.epa.epabackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
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
    public Recommendation create(String stringRecommendation, Long questionnaireId, Long evaluatedId,
                                 String senderEmail) {
    log.info("Сохранение рекомендации {}", stringRecommendation);
        Employee recipient = employeeService.findById(evaluatedId);
        Employee sender = employeeService.findByEmail(senderEmail);
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        Recommendation recommendation = recommendationMapper.mapToEntity(stringRecommendation, questionnaire,
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
     * Получение всех рекомендаций.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Recommendation> findAll() {
        log.info("Получение всех рекомендаций");
        return recommendationRepository.findAll();
    }
}
