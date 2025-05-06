package com.pmu.courses_manager.application.exception;

public class CourseInexistanteException extends RuntimeException {
    public CourseInexistanteException(String message) {
        super(message);
    }
}
