package ru.epa.epabackend.dto.questionnaire;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.epa.epabackend.dto.criteria.RequestCriteriaDto;

import java.util.List;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс QuestionnaireRequestDto для создания или обновления анкеты со списком критериев
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class RequestQuestionnaireDto {
    @Positive
    private long id;

    @Valid
    @NotEmpty(message = "Должен быть заполнен хотя бы 1 критерий", groups = {Create.class, Update.class})
    private List<RequestCriteriaDto> criterias;
}
