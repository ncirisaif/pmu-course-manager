package com.pmu.courses_manager.application.exception;

public class ParticipantInexistantException extends RuntimeException {
    public ParticipantInexistantException(String message) {
        super(message);
    }
}