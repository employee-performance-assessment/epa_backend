package ru.epa.epabackend.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.exception.exceptions.NotFoundException;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.ProjectRepository;

import static ru.epa.epabackend.exception.ExceptionDescriptions.PROJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl {

    ProjectRepository projectRepository;

    public Project findByID(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(PROJECT_NOT_FOUND.getTitle()));
    }
}