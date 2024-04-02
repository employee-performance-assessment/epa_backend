package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndExecutorId(Long taskId, Long employeeId);

    List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status);

    List<Task> findAllByExecutorIdAndStatus(Long employeeId, TaskStatus status);

    List<Task> findAllByExecutorId(Long employeeId);

    List<Task> findAllByOwnerEmail(String email);

    List<Task> findAllByOwnerEmailAndExecutorId(String email, Long executorId);

    List<Task> findAllByOwnerIdAndFinishDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Task> findAllByExecutorIdAndFinishDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}