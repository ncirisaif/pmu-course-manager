package com.pmu.courses_manager.application.api.request;


import jakarta.validation.constraints.Positive;

public record UpdateParticipantRequest(
        String nom,
        String prenom,

        @Positive(message = "Le dossard doit être positif")
        Integer dossard
) {}