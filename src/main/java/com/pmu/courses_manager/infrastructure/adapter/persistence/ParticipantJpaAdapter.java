package com.pmu.courses_manager.infrastructure.adapter.persistence;


import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import com.pmu.courses_manager.domain.port.out.ParticipantPersistencePort;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.CourseJpaEntity;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.ParticipantJpaEntity;
import com.pmu.courses_manager.infrastructure.adapter.persistence.mapper.ParticipantMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Adaptateur de persistance pour les participants (JPA)
 */
@Service
public class ParticipantJpaAdapter implements ParticipantPersistencePort {

    private final ParticipantJpaRepository participantRepository;
    private final CourseJpaRepository courseJpaRepository;

    private final ParticipantMapper participantMapper;

    public ParticipantJpaAdapter(
            ParticipantJpaRepository participantRepository, CourseJpaRepository courseJpaRepository,
            ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.courseJpaRepository = courseJpaRepository;
        this.participantMapper = participantMapper;
    }

    @Override
    public Participant save(CourseId courseId, Participant participant) {
        CourseJpaEntity courseJpaEntity = courseJpaRepository.findById(courseId.getValue())
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));

        ParticipantJpaEntity participantJpaEntity = participantMapper.toEntity(participant);
        participantJpaEntity.setCourse(courseJpaEntity);
        ParticipantJpaEntity savedEntity = participantRepository.save(participantJpaEntity);
        return participantMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Participant> findById(ParticipantId participantId) {
        return participantRepository.findById(participantId.getValue())
                .map(participantMapper::toDomain);
    }
}


