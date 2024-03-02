package ru.epa.epabackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.exception.exceptions.NotFoundException;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.service.ProjectService;

import static ru.epa.epabackend.exception.ExceptionDescriptions.PROJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public Project findByID(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(PROJECT_NOT_FOUND.getTitle()));
    }
}