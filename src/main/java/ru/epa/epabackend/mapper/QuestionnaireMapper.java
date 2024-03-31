package ru.epa.epabackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireFullDto;
import ru.epa.epabackend.dto.questionnaire.ResponseQuestionnaireShortDto;
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
    Questionnaire mapToEntity(ResponseQuestionnaireShortDto responseQuestionnaireShortDto);

    @Mapping(target = "id", ignore = true)
    Questionnaire mapToEntity(List<Criteria> criterias, Employee author, QuestionnaireStatus status);

    Questionnaire mapToEntity(ResponseQuestionnaireFullDto responseQuestionnaireFullDto);

    ResponseQuestionnaireShortDto mapToShortResponseDto(Questionnaire questionnaire);

    ResponseQuestionnaireFullDto mapToFullResponseDto(Questionnaire questionnaire);

    Questionnaire mapToEntity(List<Criteria> criterias, Employee author, QuestionnaireStatus status,
                              long id);

    List<ResponseQuestionnaireShortDto> mapToList(List<Questionnaire> questionnairies);
}
