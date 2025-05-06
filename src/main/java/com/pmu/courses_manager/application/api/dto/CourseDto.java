package com.pmu.courses_manager.application.api.dto;

import java.time.LocalDate;

public record CourseDto(
        Long id,
        String nom,
        LocalDate date,
        Integer numero
) {}