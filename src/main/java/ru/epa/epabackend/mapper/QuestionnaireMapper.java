package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireFullResponseDto;
import ru.epa.epabackend.dto.questionnaire.QuestionnaireShortResponseDto;
import ru.epa.epabackend.model.Criteria;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Questionnaire;
import ru.epa.epabackend.util.QuestionnaireStatus;

import java.util.List;

/**
 * Интерфейс QuestionnaireMapper для преобразования анкет из сущности в Dto и обратно
 */
@Mapper(componentModel = "spring")
public interface QuestionnaireMapper {
    Questionnaire mapToEntity(QuestionnaireShortResponseDto questionnaireShortResponseDto);

    @Mapping(target = "id", ignore = true)
    Questionnaire mapToEntity(List<Criteria> criterias, Employee author, QuestionnaireStatus status);

    Questionnaire mapToEntity(QuestionnaireFullResponseDto questionnaireFullResponseDto);

    QuestionnaireShortResponseDto mapToShortResponseDto(Questionnaire questionnaire);

    QuestionnaireFullResponseDto mapToFullResponseDto(Questionnaire questionnaire);

    Questionnaire mapToEntity(List<Criteria> criterias, Employee author, QuestionnaireStatus status,
                              long id);

    List<QuestionnaireShortResponseDto> mapToList(List<Questionnaire> questionnairies);
}