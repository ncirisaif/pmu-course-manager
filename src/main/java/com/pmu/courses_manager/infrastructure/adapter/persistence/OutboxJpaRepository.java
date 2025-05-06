package com.pmu.courses_manager.infrastructure.adapter.persistence;

import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxJpaRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findAllBySentFalse();
}
