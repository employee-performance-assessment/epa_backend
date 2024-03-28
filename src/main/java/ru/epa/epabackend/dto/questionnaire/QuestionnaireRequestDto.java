package ru.epa.epabackend.dto.questionnaire;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.dto.evaluation.CriteriaRequestDto;

import java.util.Set;

import static ru.epa.epabackend.util.ValidationGroups.Create;
import static ru.epa.epabackend.util.ValidationGroups.Update;

/**
 * Класс QuestionnaireRequestDto для создания или обновления анкеты со списком критериев
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireRequestDto {

    @NotEmpty(message = "Должен быть заполнен хотя бы 1 критерий", groups = {Create.class, Update.class})
    private Set<CriteriaRequestDto> criterias;
}
