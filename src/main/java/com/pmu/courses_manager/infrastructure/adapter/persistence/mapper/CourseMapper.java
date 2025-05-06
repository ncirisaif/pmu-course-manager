package com.pmu.courses_manager.infrastructure.adapter.persistence.mapper;

import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.CourseJpaEntity;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.ParticipantJpaEntity;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper entre les entités JPA et les objets du domaine
 */
@Component
public class CourseMapper {

    public CourseJpaEntity toEntity(Course domain) {
        CourseJpaEntity entity = new CourseJpaEntity();

        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }

        entity.setNom(domain.getNom());
        entity.setDate(domain.getDate());
        entity.setNumero(domain.getNumero());

        Set<ParticipantJpaEntity> participantEntities = domain.getParticipants().stream()
                .map(this::toParticipantEntity)
                .collect(Collectors.toSet());

        entity.setParticipants(new HashSet<>());
        participantEntities.forEach(entity::addParticipant);

        return entity;
    }

    public ParticipantJpaEntity toParticipantEntity(Participant domain) {
        ParticipantJpaEntity entity = new ParticipantJpaEntity();

        if (domain.getId() != null) {
            entity.setId(domain.getId().getValue());
        }

        entity.setNom(domain.getNom());
        entity.setDossard(domain.getDossard());

        return entity;
    }

    public Course toDomain(CourseJpaEntity entity) {
        Course course = Course.reconstitute(
                new CourseId(entity.getId()),
                entity.getNom(),
                entity.getDate(),
                entity.getNumero());

        for (ParticipantJpaEntity participantEntity : entity.getParticipants()) {
            Participant participant = toParticipantDomain(participantEntity);
            course.addParticipant(participant.getNom(), participant.getDossard());
        }
        return course;
    }

    public Participant toParticipantDomain(ParticipantJpaEntity entity) {
        Participant participant = Participant.create(entity.getNom(), entity.getDossard());

        try {
            java.lang.reflect.Field idField = Participant.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(participant, new ParticipantId(entity.getId()));
            idField.setAccessible(false);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du mapping d'entité vers domaine", e);
        }
        return participant;
    }
}