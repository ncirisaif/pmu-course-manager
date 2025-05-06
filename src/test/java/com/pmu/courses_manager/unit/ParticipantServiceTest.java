package com.pmu.courses_manager.unit;

import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.application.exception.ParticipantInexistantException;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import com.pmu.courses_manager.domain.port.out.CourseEventPort;
import com.pmu.courses_manager.domain.port.out.CoursePersistencePort;
import com.pmu.courses_manager.domain.port.out.ParticipantPersistencePort;
import com.pmu.courses_manager.domain.service.ParticipantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private CoursePersistencePort coursePersistencePort;

    @Mock
    private CourseEventPort courseEventPort;

    @Mock
    private ParticipantPersistencePort participantPersistencePort;

    @InjectMocks
    private ParticipantService participantService;

    private CourseId courseId;
    private Course course;
    private Participant participant;
    private ParticipantId participantId;

    @BeforeEach
    void setUp() {
        courseId = new CourseId(1L);
        participantId = new ParticipantId(1L);
        participant = Participant.create("Test Participant", 42);
        course = createTestCourse();
    }

    private Course createTestCourse() {
        return Course.create("Test Course", LocalDate.now(), 1);
    }

    @Test
    @DisplayName("Doit ajouter un participant avec succès")
    void shouldAddParticipantSuccessfully() {
        // Given
        String nom = "Nouveau Participant";

        when(coursePersistencePort.findById(courseId)).thenReturn(Optional.of(course));
        when(participantPersistencePort.save(eq(courseId), any(Participant.class)))
                .thenAnswer(invocation -> {
                    Participant savedParticipant = invocation.getArgument(1);
                    return Participant.reconstitute(new ParticipantId(2L), savedParticipant.getNom(), savedParticipant.getDossard());
                });

        // When
        ParticipantId result = participantService.addParticipant(courseId, nom);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getValue());
        ArgumentCaptor<Participant> participantCaptor = ArgumentCaptor.forClass(Participant.class);
        verify(participantPersistencePort).save(eq(courseId), participantCaptor.capture());
        Participant capturedParticipant = participantCaptor.getValue();
        assertEquals(nom, capturedParticipant.getNom());
        verify(courseEventPort).publishParticipantAdded(eq(courseId), any(Participant.class));
    }

    @Test
    @DisplayName("Doit lever une exception lorsque la course n'existe pas")
    void shouldThrowExceptionWhenCourseNotFound() {
        // Given
        String nom = "Nouveau Participant";
        Integer dossard = 99;

        when(coursePersistencePort.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CourseInexistanteException.class, () -> {
            participantService.addParticipant(courseId, nom);
        });

        verify(coursePersistencePort).findById(courseId);
        verify(participantPersistencePort, never()).save(any(CourseId.class), any(Participant.class));
        verify(courseEventPort, never()).publishParticipantAdded(any(CourseId.class), any(Participant.class));
    }

    @Test
    @DisplayName("Doit récupérer un participant par son ID")
    void shouldGetParticipantById() {
        // Given
        when(participantPersistencePort.findById(participantId)).thenReturn(Optional.of(participant));

        // When
        Participant result = participantService.getParticipantById(participantId);

        // Then
        assertNotNull(result);
        assertEquals(participant, result);
        verify(participantPersistencePort).findById(participantId);
    }

    @Test
    @DisplayName("Doit lever une exception lorsque le participant n'existe pas")
    void shouldThrowExceptionWhenParticipantNotFound() {
        // Given
        when(participantPersistencePort.findById(participantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ParticipantInexistantException.class, () -> {
            participantService.getParticipantById(participantId);
        });

        verify(participantPersistencePort).findById(participantId);
    }

    @Test
    @DisplayName("Doit récupérer tous les participants d'une course")
    void shouldGetAllParticipantsByCourse() {
        // Given
        when(coursePersistencePort.findById(courseId)).thenReturn(Optional.of(course));

        // When
        List<Participant> result = participantService.getParticipantsByCourse(courseId);

        // Then
        assertNotNull(result);
        verify(coursePersistencePort).findById(courseId);
    }
}