package com.pmu.courses_manager.domain.port.in;

import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;

import java.time.LocalDate;
import java.util.List;

/**
 * Port d'entrée pour la gestion des courses
 */
public interface CourseManagementUseCase {

    /**
     * Crée une nouvelle course
     */
    CourseId createCourse(String nom, LocalDate date, Integer numero);

    /**
     * Met à jour une course existante
     */
    void updateCourse(CourseId courseId, String nom, LocalDate date, Integer numero);

    /**
     * Supprime une course
     */
    void deleteCourse(CourseId courseId);

    /**
     * Récupère une course par son identifiant
     */
    Course getCourseById(CourseId courseId);

    /**
     * Liste toutes les courses
     */
    List<Course> getAllCourses();
}