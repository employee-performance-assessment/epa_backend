package ru.epa.epabackend.service.impl;

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

    @Override
    @Transactional(readOnly = true)
    public Recommendation getByRecipientIdAndQuestionnaireId(Long evaluatedId, Long questionnaireId) {
        return recommendationRepository.getByRecipientIdAndQuestionnaireId(evaluatedId, questionnaireId);
    }
}
