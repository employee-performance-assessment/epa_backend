package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.util.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndExecutorId(Long taskId, Long employeeId);

    List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status);

    @Query("SELECT t " +
            "FROM Task t " +
            "WHERE (NULLIF((:status), NULL) IS NULL OR t.status = :status)" +
            "AND t.executor.id = :employeeId ")
    List<Task> findAllByExecutorIdFilters(Long employeeId, TaskStatus status);

    List<Task> findAllByOwnerEmail(String email);

    List<Task> findAllByOwnerIdAndFinishDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Task> findAllByExecutorIdAndFinishDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}