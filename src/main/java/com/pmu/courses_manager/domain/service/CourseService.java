package com.pmu.courses_manager.domain.service;


import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.application.exception.CourseExisteDejaException;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.domain.port.out.CourseEventPort;
import com.pmu.courses_manager.domain.port.in.CourseManagementUseCase;
import com.pmu.courses_manager.domain.port.out.CoursePersistencePort;
import com.pmu.courses_manager.domain.port.out.OutboxEventPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service d'application pour la gestion des courses
 */
@Service
public class CourseService implements CourseManagementUseCase {

    private final CoursePersistencePort coursePersistencePort;
    private final CourseEventPort courseEventPort;
    private final OutboxEventPersistencePort outboxEventPersistencePort;

    public CourseService(CoursePersistencePort coursePersistencePort, CourseEventPort courseEventPort, OutboxEventPersistencePort outboxEventPersistencePort) {
        this.coursePersistencePort = coursePersistencePort;
        this.courseEventPort = courseEventPort;
        this.outboxEventPersistencePort = outboxEventPersistencePort;
    }

    @Override
    @Transactional
    public CourseId createCourse(String nom, LocalDate date, Integer numero) {
        if (coursePersistencePort.existsByDateAndNumero(date, numero)) {
            throw new CourseExisteDejaException(
                    "Une course avec la date " + date + " et le numéro " + numero + " existe déjà");
        }
        Course course = Course.create(nom, date, numero);
        Course savedCourse = coursePersistencePort.save(course);
        outboxEventPersistencePort.saveCreatedCourseEvent(savedCourse);

        return savedCourse.getId();
    }

    @Override
    @Transactional
    public void updateCourse(CourseId courseId, String nom, LocalDate date, Integer numero) {
        Course course = coursePersistencePort.findById(courseId)
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));

        if (date != null && numero != null &&
                !date.equals(course.getDate()) && !numero.equals(course.getNumero()) &&
                coursePersistencePort.existsByDateAndNumero(date, numero)) {
            throw new CourseExisteDejaException("Une course avec la date " + date + " et le numéro " + numero + " existe déjà");
        }

        course.updateDetails(nom, date, numero);
        Course updatedCourse = coursePersistencePort.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(CourseId courseId) {
        if (!coursePersistencePort.findById(courseId).isPresent()) {
            throw new CourseInexistanteException("Course non trouvée avec l'id : " + courseId);
        }
        coursePersistencePort.delete(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Course getCourseById(CourseId courseId) {
        return coursePersistencePort.findById(courseId)
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return coursePersistencePort.findAll();
    }
}

