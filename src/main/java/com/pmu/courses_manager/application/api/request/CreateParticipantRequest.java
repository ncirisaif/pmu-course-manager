package com.pmu.courses_manager.application.api.request;


import jakarta.validation.constraints.NotBlank;

public record CreateParticipantRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom) {}