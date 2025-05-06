package com.pmu.courses_manager.domain.port.out;

import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;

/**
 * Port de sortie pour l'événementiel
 */
public interface CourseEventPort {

    /**
     * Publie un événement de création de course
     */
    void publishCourseCreated(Course course);

    /**
     * Publie un événement d'ajout de participant
     */
    void publishParticipantAdded(CourseId courseId, Participant participant);
}