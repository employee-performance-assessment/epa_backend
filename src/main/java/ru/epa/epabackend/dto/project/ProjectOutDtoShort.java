package ru.epa.epabackend.dto.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.epa.epabackend.util.ProjectStatus;

@Builder
@Data
@AllArgsConstructor
public class ProjectOutDtoShort {

    private String name;

    private ProjectStatus status;
}
