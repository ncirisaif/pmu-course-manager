package com.pmu.courses_manager.infrastructure.adapter.persistence.mapper;

import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.ParticipantJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre les entités JPA et les objets du domaine Participant
 */
@Component
public class ParticipantMapper {

    public ParticipantJpaEntity toEntity(Participant domain) {
        ParticipantJpaEntity entity = new ParticipantJpaEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }
        entity.setNom(domain.getNom());
        entity.setDossard(domain.getDossard());
        return entity;
    }

    public Participant toDomain(ParticipantJpaEntity entity) {
        return Participant.reconstitute(
                new ParticipantId(entity.getId()),
                entity.getNom(),
                entity.getDossard()
        );
    }
}
