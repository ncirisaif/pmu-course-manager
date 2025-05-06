package com.pmu.courses_manager.infrastructure.adapter.messaging;

/**
 * Exception lancée en cas d'échec de publication d'événement
 */
class EventPublicationException extends RuntimeException {
    public EventPublicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
