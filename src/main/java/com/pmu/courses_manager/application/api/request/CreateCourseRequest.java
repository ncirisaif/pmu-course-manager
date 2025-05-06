package com.pmu.courses_manager.application.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Classes DTO pour les requêtes et les réponses
 */
public record CreateCourseRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotNull(message = "La date est obligatoire")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,

        @NotNull(message = "Le numéro est obligatoire")
        @Positive(message = "Le numéro doit être positif")
        Integer numero
) {}

