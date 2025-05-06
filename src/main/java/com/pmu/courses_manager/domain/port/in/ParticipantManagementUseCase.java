package com.pmu.courses_manager.domain.port.in;

import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;

import java.util.List;

/**
 * Port d'entrée pour la gestion des participants
 */
public interface ParticipantManagementUseCase {
    /**
     * Ajoute un participant à une course
     */
    ParticipantId addParticipant(CourseId courseId, String nom);

    /**
     * Récupère un participant par son identifiant
     */
    Participant getParticipantById(ParticipantId participantId);

    /**
     * Liste tous les participants d'une course
     */
    List<Participant> getParticipantsByCourse(CourseId courseId);

}



