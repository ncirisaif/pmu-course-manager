package com.pmu.courses_manager.infrastructure.adapter.persistence;

import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.CourseJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour les entités Course
 */
interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, Long> {

    @Query("SELECT c FROM CourseJpaEntity c LEFT JOIN FETCH c.participants WHERE c.id = :id")
    Optional<CourseJpaEntity> findByIdWithParticipants(@Param("id") Long id);

    boolean existsByDateAndNumero(LocalDate date, Integer numero);
}


