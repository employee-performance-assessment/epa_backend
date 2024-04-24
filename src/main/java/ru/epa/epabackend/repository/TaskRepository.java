package ru.epa.epabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Task> findAllByOwnerIdAndFinishDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Task> findAllByOwnerEmailAndFinishDateBetween(String email, LocalDate startDate, LocalDate endDate);

    List<Task> findAllByExecutorIdAndFinishDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT SUM(t.points) " +
            "from Task t " +
            "where t.executor.id = :employeeId " +
            "and t.finishDate BETWEEN :rangeStart AND :rangeEnd ")
    Integer getSumPointsByExecutorIdAndForCurrentMonth(Long employeeId, LocalDate rangeStart, LocalDate rangeEnd);

    Boolean existsByProjectId(Long projectId);

    List<Task> findAllByProjectId(Long projectId);

    List<Task> findAllByOwnerId(Long ownerId);

    boolean existsByExecutorId(Long employeeId);

    boolean existsByIdAndOwnerEmail(Long taskId, String email);

    Optional<Task> findByIdAndOwnerId(Long taskId, Long id);

    @Query("SELECT t " +
            "FROM Task t " +
            "WHERE t.owner.email = :email " +
            "AND t.executor.id = :employeeId " +
            "AND ((nullif((cast(:text as text)), null) is null or LOWER(t.name) LIKE %:text%) " +
            "OR (nullif((cast(:text as text)), null) is null or LOWER(t.description) LIKE %:text%))")
    List<Task> findTasksByOwnerEmailAndExecutorIdAndText(String email, Long employeeId, String text);

    @Query("SELECT t " +
            "FROM Task t " +
            "WHERE t.owner.email = :email " +
            "AND t.executor.id = :employeeId " +
            "AND t.status = :status " +
            "AND ((nullif((cast(:text as text)), null) is null or LOWER(t.name) LIKE %:text%) " +
            "OR (nullif((cast(:text as text)), null) is null or LOWER(t.description) LIKE %:text%))")
    List<Task> findTasksByOwnerEmailAndExecutorIdAndStatusAndText(String email, Long employeeId, TaskStatus status,
                                                                  String text);

    @Query("SELECT t " +
            "FROM Task t " +
            "WHERE t.executor.id = :employeeId " +
            "AND ((nullif((cast(:text as text)), null) is null or LOWER(t.name) LIKE %:text%) " +
            "OR (nullif((cast(:text as text)), null) is null or LOWER(t.description) LIKE %:text%))")
    List<Task> findAllByExecutorIdAndText(Long employeeId, String text);

    @Query("SELECT t " +
            "FROM Task t " +
            "WHERE t.executor.id = :employeeId " +
            "AND t.status = :status " +
            "AND ((nullif((cast(:text as text)), null) is null or LOWER(t.name) LIKE %:text%) " +
            "OR (nullif((cast(:text as text)), null) is null or LOWER(t.description) LIKE %:text%))")
    List<Task> findAllByExecutorIdAndStatusAndText(Long employeeId, TaskStatus status, String text);
}