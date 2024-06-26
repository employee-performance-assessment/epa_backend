package ru.epa.epabackend.util;

/**
 * Статусы жизни проекта
 *
 * @author Михаил Безуглов
 */
public enum ProjectStatus {

    /**
     * Проект заброшен или заархивирован
     */
    ABANDONED,
    /**
     * Проект запланирован
     */
    TODO,
    /**
     * Над проектом ведется работа
     */
    WIP,
    /**
     * Проект закончен
     */
    COMPLETED,
    /**
     * Проект опубликован/презентован/распространён
     */
    DISTRIBUTED
}
