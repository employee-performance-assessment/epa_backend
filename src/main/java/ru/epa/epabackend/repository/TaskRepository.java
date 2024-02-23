package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
