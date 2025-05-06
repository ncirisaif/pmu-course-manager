package com.pmu.courses_manager.domain.service;

import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.application.exception.ParticipantInexistantException;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import com.pmu.courses_manager.domain.port.in.ParticipantManagementUseCase;
import com.pmu.courses_manager.domain.port.out.CourseEventPort;
import com.pmu.courses_manager.domain.port.out.CoursePersistencePort;
import com.pmu.courses_manager.domain.port.out.OutboxEventPersistencePort;
import com.pmu.courses_manager.domain.port.out.ParticipantPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service d'application pour la gestion des participants
 */
@Service
public class ParticipantService implements ParticipantManagementUseCase {

    private final CoursePersistencePort coursePersistencePort;
    private final CourseEventPort courseEventPort;
    private final ParticipantPersistencePort participantPersistencePort;
    private final OutboxEventPersistencePort outboxEventPersistencePort;


    public ParticipantService(CoursePersistencePort coursePersistencePort, CourseEventPort courseEventPort, ParticipantPersistencePort participantPersistencePort, OutboxEventPersistencePort outboxEventPersistencePort) {
        this.coursePersistencePort = coursePersistencePort;
        this.courseEventPort = courseEventPort;
        this.participantPersistencePort = participantPersistencePort;
        this.outboxEventPersistencePort = outboxEventPersistencePort;
    }

    @Override
    @Transactional
    public ParticipantId addParticipant(CourseId courseId, String nom) {
        coursePersistencePort.findById(courseId)
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));

       var dossard = getNextParticipantNumber(courseId);

        Participant savedParticipant = participantPersistencePort.save(courseId, Participant.create(nom, dossard));
        outboxEventPersistencePort.saveAddedParticipantEvent(courseId, savedParticipant);
        return savedParticipant.getId();
    }

    private Integer getNextParticipantNumber(CourseId courseId) {
        Course course = coursePersistencePort.findById(courseId)
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));
        if (course.getParticipants().isEmpty()) {
            return 1;
        }
        return course.getParticipants().stream()
                .map(Participant::getDossard)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }

    @Override
    @Transactional(readOnly = true)
    public Participant getParticipantById(ParticipantId participantId) {
        return participantPersistencePort.findById(participantId).orElseThrow(()-> new ParticipantInexistantException("Participant non trouvé avec l'id : " + participantId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Participant> getParticipantsByCourse(CourseId courseId) {
        Course course = coursePersistencePort.findById(courseId)
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));
        return List.copyOf(course.getParticipants());
    }
}