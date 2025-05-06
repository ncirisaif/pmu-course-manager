package com.pmu.courses_manager.domain.port.out;

import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Port de sortie pour la persistance des courses
 */
public interface CoursePersistencePort {

    /**
     * Enregistre une course (création ou mise à jour)
     */
    Course save(Course course);

    /**
     * Supprime une course
     */
    void delete(CourseId courseId);

    /**
     * Charge une course par son identifiant
     */
    Optional<Course> findById(CourseId courseId);

    /**
     * Vérifie si une course existe avec la date et le numéro donnés
     */
    boolean existsByDateAndNumero(LocalDate date, Integer numero);

    /**
     * Liste toutes les courses
     */
    List<Course> findAll();
}
