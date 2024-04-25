package ru.epa.epabackend.util;

/**
 * Роли сотрудников для определения прав просмотра и редактирования данных
 *
 * @author Михаил Безуглов
 */
public enum Role {
    /**
     * Руководитель
     */
    ROLE_ADMIN,
    /**
     * Сотрудник
     */
    ROLE_USER
}
