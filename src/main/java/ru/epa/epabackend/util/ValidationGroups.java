package ru.epa.epabackend.util;

import jakarta.validation.groups.Default;

public interface ValidationGroups {

    interface Create extends Default {
    }

    interface Update extends Default {
    }
}
