package com.pmu.courses_manager.domain.model;

import java.util.Objects;

/**
 * Value Object pour l'identifiant d'une course
 */

public class CourseId {
    private final Long value;

    private CourseId() {
        this.value = null;
    }

    public CourseId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("L'identifiant de course ne peut pas être null");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseId courseId = (CourseId) o;
        return Objects.equals(value, courseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
