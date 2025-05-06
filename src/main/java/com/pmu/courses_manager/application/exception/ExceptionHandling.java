package com.pmu.courses_manager.application.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Hidden
public class ExceptionHandling {
    @ExceptionHandler(CourseExisteDejaException.class)
    public ResponseEntity<ErrorResponse> handleCourseExisteDejaException(CourseExisteDejaException ex) {
        ErrorResponse error = new ErrorResponse("COURSE_DUPLICATED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(CourseInexistanteException.class)
    public ResponseEntity<ErrorResponse> handleCourseInexistanteException(CourseInexistanteException ex) {
        ErrorResponse error = new ErrorResponse("COURSE_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ParticipantInexistantException.class)
    public ResponseEntity<ErrorResponse> handleParticipantInexistantException(ParticipantInexistantException ex) {
        ErrorResponse error = new ErrorResponse("PARTICIPANT_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DossardDejaUtiliseException.class)
    public ResponseEntity<ErrorResponse> handleDossardDejaUtiliseException(DossardDejaUtiliseException ex) {
        ErrorResponse error = new ErrorResponse("DOSSARD_ALREADY_USED", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleDossardDejaUtiliseException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse("ILLEGAL_ARGUMENT", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
