package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByExecutorId(Long employeeId);

    Optional<Task> findByIdAndExecutorId(Long taskId, Long employeeId);

}
