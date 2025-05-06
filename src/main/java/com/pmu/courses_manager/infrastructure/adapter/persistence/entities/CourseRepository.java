package com.pmu.courses_manager.infrastructure.adapter.persistence.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Course utilisant Spring Data JPA
 */
@Repository
public interface CourseRepository extends JpaRepository<CourseJpaEntity, Long> {
}
