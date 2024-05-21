package ru.epa.epabackend.service;

import org.springframework.transaction.annotation.Transactional;
import ru.epa.epabackend.dto.task.RequestTaskDto;
import ru.epa.epabackend.model.Task;
import ru.epa.epabackend.util.TaskStatus;

import java.security.Principal;
import java.util.List;

/**
 * Интерфейс TaskService содержит методы действий с задачами
 *
 * @author Владислав Осипов
 */
public interface TaskService {

    /**
     * Получение списка задач по email исполнителя
     */
    List<Task> findAllByExecutorEmail(Principal principal, String text);

    /**
     * Получение списка задач по email исполнителя и статусу задачи
     */
    List<Task> findAllByExecutorEmailAndStatus(String status, Principal principal, String text);

    /**
     * Получение списка задач админом по id исполнителя
     */
    List<Task> findAllByEmployeeId(Long employeeId, String email, String text);

    /**
     * Получение списка задач админом по id исполнителя и статусу задачи
     */
    List<Task> findAllByEmployeeIdAndStatus(Long employeeId, String email, String status, String text);

    /**
     * Получение задачи по id задачи и исполнителю
     */
    Task findByIdAndOwnerId(Principal principal, Long taskId);

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
     * Добавление задачи
     */
    Task create(RequestTaskDto taskDto, String email);

    /**
     * Получение задачи по id
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
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status, String email);

    @Transactional(readOnly = true)
    Task findById(Long taskId);

    List<Task> findAllForEmployeeByProjectId(String email, Long projectId);

    List<Task> findAllForEmployee(String email);
}