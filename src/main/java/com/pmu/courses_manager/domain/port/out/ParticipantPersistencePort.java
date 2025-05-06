package com.pmu.courses_manager.domain.port.out;

import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;

import java.util.Optional;

/**
 * Port de sortie pour la persistance des participant
 */
public interface ParticipantPersistencePort {

    /**
     * Enregistre une Participant
     */
    Participant save(CourseId courseId, Participant participant);

    /**
     * Charge un Participant par son identifiant
     */
    Optional<Participant> findById(ParticipantId participantId);
}
