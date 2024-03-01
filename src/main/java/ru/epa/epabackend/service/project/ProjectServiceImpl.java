package ru.epa.epabackend.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.project.NewProjectRto;
import ru.epa.epabackend.dto.project.ProjectEmployeesDto;
import ru.epa.epabackend.dto.project.ProjectShortDto;
import ru.epa.epabackend.exception.exceptions.NotFoundException;
import ru.epa.epabackend.mapper.ProjectMapper;
import ru.epa.epabackend.model.Project;
import ru.epa.epabackend.repository.ProjectRepository;
import ru.epa.epabackend.service.EmployeeService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.epa.epabackend.exception.ExceptionDescriptions.PROJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final EmployeeService employeeService;

    private Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException(PROJECT_NOT_FOUND.getTitle()));
    }

    @Override
    public ProjectShortDto save(NewProjectRto newProjectRto) {
        return projectMapper.toProjectShortDto(projectRepository.save(projectMapper.toProject(newProjectRto)));
    }

    @Override
    public ProjectShortDto findDtoById(Long id) {
        return projectMapper.toProjectShortDto(findById(id));
    }

    @Override
    public ProjectEmployeesDto saveWithEmployee(Long projectId, Long employeeId) {
        Project project = findById(projectId).setId(projectId);
        return projectMapper.toProjectEmployeesDto(projectRepository.save(project));
    }

    @Override
    public List<ProjectShortDto> findByAdminId(Long adminId) {
        return projectRepository.findByEmployees(List.of(employeeService.getEmployee(adminId))).stream()
                .map(projectMapper::toProjectShortDto).collect(Collectors.toList());
    }
}