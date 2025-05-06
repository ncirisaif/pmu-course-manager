package com.pmu.courses_manager.infrastructure.adapter.persistence;



import com.pmu.courses_manager.infrastructure.adapter.persistence.mapper.CourseMapper;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.CourseJpaEntity;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.port.out.CoursePersistencePort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adaptateur de persistance pour les courses (JPA)
 */
@Repository
public class CourseJpaAdapter implements CoursePersistencePort {

    private final CourseJpaRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseJpaAdapter(CourseJpaRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public Course save(Course course) {
        CourseJpaEntity courseEntity = courseMapper.toEntity(course);
        CourseJpaEntity savedEntity = courseRepository.save(courseEntity);
        return courseMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(CourseId courseId) {
        courseRepository.deleteById(courseId.getValue());
    }

    @Override
    public Optional<Course> findById(CourseId courseId) {
        return courseRepository.findByIdWithParticipants(courseId.getValue())
                .map(courseMapper::toDomain);
    }

    @Override
    public boolean existsByDateAndNumero(LocalDate date, Integer numero) {
        return courseRepository.existsByDateAndNumero(date, numero);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDomain)
                .collect(Collectors.toList());
    }
}

