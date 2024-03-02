package ru.epa.epabackend.dto.project;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.epa.epabackend.util.ProjectStatus;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateProjectRto {
    @Size(min = 3, max = 255)
    private String name;
    private ProjectStatus status;
}
