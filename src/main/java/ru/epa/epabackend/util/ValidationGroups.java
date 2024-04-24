package ru.epa.epabackend.util;

import jakarta.validation.groups.Default;

/**
 * Утилитный класс ValidationGroups для разделения валидации входящих данных при создании и обновлении
 *
 * @author Валентина Вахламова
 */
public interface ValidationGroups {

    interface Create extends Default {
    }

    interface Update extends Default {
    }
}
