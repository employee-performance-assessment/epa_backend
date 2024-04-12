package ru.epa.epabackend.service;

import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.RequestTaskDto;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Интерфейс TaskService содержит методы действий с задачами .
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    /**
     * Получение списка задач по email исполнителя
     */
    List<Task> findAllByExecutorEmail(Principal principal);

    /**
     * Получение списка задач по email исполнителя и статусу задачи
     */
    List<Task> findAllByExecutorEmailAndStatus(String status, Principal principal);

    /**
     * Получение списка задач администратором по ID исполнителя
     */
    List<Task> findAllByEmployeeId(Long employeeId, String email);

    /**
     * Получение списка задач администратором по ID исполнителя и статусу задачи
     */
    List<Task> findAllByEmployeeIdAndStatus(Long employeeId, String email, String status);

    /**
     * Найти задачу по ID задачи и ID исполнителя
     */
    Task findByIdAndExecutorEmail(Principal principal, Long taskId);

    /**
     * Обновление статуса задачи
     */
    Task updateStatus(Long taskId, String status, Principal principal);

    /**
     * Получение списка всех задач
     */
    List<Task> findAll(String email);

    /**
     * Получение списка всех задач по определенному проекту админом
     */
    List<Task> findAllByProjectId(String email, Long projectId);

    /**
     * Создание задачи
     */
    Task create(RequestTaskDto taskDto, String email);

    /**
     * Найти задачу по ID
     */
    Task findDtoById(Long taskId, String email);

    /**
     * Обновление задачи
     */
    Task update(Long taskId, RequestTaskDto taskDto, String email);

    /**
     * Удаление задачи
     */
    void delete(Long taskId, String email);

    /**
     * Получение списка задач проекта с определенным статусом задач
     */
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);

    @Transactional(readOnly = true)
    Task findByIdAndExecutorEmail(Long taskId, Long employeeId);

    @Transactional(readOnly = true)
    Task findById(Long taskId);

    List<Task> findAllForEmployeeByProjectId(String email, Long projectId);

    List<Task> findAllForEmployee(String email);
}