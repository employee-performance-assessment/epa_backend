package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.util.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndExecutorId(Long taskId, Long employeeId);

    List<Task> findAllByProjectIdAndStatus(Long projectId, TaskStatus status);

    @Query("select t " +
            "from Task t " +
            "where (nullif((:status), null) is null or t.status = :status)" +
            "and t.executor.id = :employeeId ")
    List<Task> findAllByExecutorIdFilters(Long employeeId, TaskStatus status);

    List<Task> findAllByOwnerEmail(String email);
}
