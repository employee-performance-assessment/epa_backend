package ru.epa.epabackend.util;

import java.util.Optional;

/**
 * Статусы жизни задачи.
 *
 * @author Михаил Безуглов
 */
public enum TaskStatus {

    /**
     * Новая задача.
     */
    NEW,
    /**
     * Задача над которой ведется работа.
     */
    IN_PROGRESS,
    /**
     * Задача на проверке у руководителя.
     */
    REVIEW,
    /**
     * Задача выполнена.
     */
    DONE,
    /**
     * Задача отменена или заморожена на неопределенный срок.
     */
    CANCELED;

    public static Optional<TaskStatus> from(String stringStatus) {
        for (TaskStatus taskStatus : values()) {
            if (taskStatus.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(taskStatus);
            }
        }
        return Optional.empty();
    }
}