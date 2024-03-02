package ru.epa.epabackend.service;


import ru.epa.epabackend.model.Project;

public interface ProjectService {

    Project findByID(Long projectId);
}
