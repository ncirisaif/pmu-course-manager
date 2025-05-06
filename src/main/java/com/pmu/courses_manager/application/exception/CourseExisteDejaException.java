package com.pmu.courses_manager.application.exception;

public class CourseExisteDejaException extends RuntimeException {
    public CourseExisteDejaException(String message) {
        super(message);
    }
}
