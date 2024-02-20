package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.epa.epabackend.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
