package ru.epa.epabackend.dto.project;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epa.epabackend.util.ProjectStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateRequestDto {
    @Size(min = 3, max = 255)
    private String name;
    private ProjectStatus status;
}
