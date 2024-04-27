package ru.epa.epabackend.service;


import ru.epa.epabackend.dto.project.RequestProjectCreateDto;
import ru.epa.epabackend.dto.project.RequestProjectCreateUpdateDto;
import ru.epa.epabackend.dto.project.RequestProjectUpdateDto;
import ru.epa.epabackend.model.Employee;
import ru.epa.epabackend.model.Project;

import java.util.List;

public interface ProjectService {
    Project findById(Long projectId);

    Project create(RequestProjectCreateDto requestProjectCreateDto, String email);

    Project findDtoById(Long projectId, String email);

    List<Project> findAllByCreator(String email);

    Project update(Long projectId, RequestProjectUpdateDto requestProjectUpdateDto, String email);

    void delete(Long projectId, String email);

    void checkUserAndProject(Employee user, Project project);

    List<Project> saveList(String email, List<RequestProjectCreateUpdateDto> requestProjectCreateUpdateDtos);
}
