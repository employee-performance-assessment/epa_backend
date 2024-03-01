package ru.epa.epabackend.exception;

public enum ExceptionDescriptions {
    TASK_NOT_FOUND("Task not found"),
    EMPLOYEE_NOT_FOUND("Employee not found"),
    FORBIDDEN_TO_EDIT_NOT_YOUR_TASK("It is forbidden to edit not your task"),
    PROJECT_NOT_FOUND("Project not found");


    private final String title;

    ExceptionDescriptions(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}