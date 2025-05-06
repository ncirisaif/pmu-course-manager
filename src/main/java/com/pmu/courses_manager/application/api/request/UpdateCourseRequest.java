package com.pmu.courses_manager.application.api.request;

import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UpdateCourseRequest(
        String nom,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,

        @Positive(message = "Le numéro doit être positif")
        Integer numero
) {}
