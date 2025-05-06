package com.pmu.courses_manager.it;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmu.courses_manager.application.api.CourseResources;
import com.pmu.courses_manager.application.api.request.CreateCourseRequest;
import com.pmu.courses_manager.application.api.request.CreateParticipantRequest;
import com.pmu.courses_manager.application.api.request.UpdateCourseRequest;
import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.application.mapper.ApplicationMapper;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import com.pmu.courses_manager.domain.port.in.CourseManagementUseCase;
import com.pmu.courses_manager.domain.port.in.ParticipantManagementUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests d'intégration pour le contrôleur REST des courses
 */
@WebMvcTest(CourseResources.class)
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseManagementUseCase courseManagementUseCase;

    @MockBean
    private ParticipantManagementUseCase participantManagementUseCase;

    @SpyBean
    private ApplicationMapper applicationMapper;

    private Course testCourse;
    private Participant testParticipant;

    @BeforeEach
    void setUp() throws Exception {
        // Préparation des objets test
        testCourse = Course.create("Course Test", LocalDate.of(2025, 5, 5), 1);
        java.lang.reflect.Field courseIdField = Course.class.getDeclaredField("id");
        courseIdField.setAccessible(true);
        courseIdField.set(testCourse, new CourseId(1L));
        courseIdField.setAccessible(false);

        testParticipant = testCourse.addParticipant("Doe", 42);

        // Simuler l'ID du participant
        java.lang.reflect.Field participantIdField = Participant.class.getDeclaredField("id");
        participantIdField.setAccessible(true);
        participantIdField.set(testParticipant, new ParticipantId(99L));
        participantIdField.setAccessible(false);
    }

    @Test
    @DisplayName("Doit créer une course avec succès")
    void shouldCreateCourseSuccessfully() throws Exception {
        // Given
        CreateCourseRequest request = new CreateCourseRequest("Nouvelle Course", LocalDate.of(2025, 6, 6), 2 );

        CourseId courseId = new CourseId(1L);
        when(courseManagementUseCase.createCourse(anyString(), any(LocalDate.class), anyInt()))
                .thenReturn(courseId);
        when(courseManagementUseCase.getCourseById(courseId)).thenReturn(testCourse);

        // When & Then
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Course Test"))
                .andExpect(jsonPath("$.date").value("2025-05-05"))
                .andExpect(jsonPath("$.numero").value(1));

        verify(courseManagementUseCase).createCourse(
                request.nom(), request.date(), request.numero());
        verify(courseManagementUseCase).getCourseById(courseId);
    }

    @Test
    @DisplayName("Doit retourner une erreur quand les données de création sont invalides")
    void shouldReturnErrorWhenCreateDataIsInvalid() throws Exception {
        // Given
        CreateCourseRequest request = new CreateCourseRequest("", null, -1);

        // When & Then
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(courseManagementUseCase, never()).createCourse(anyString(), any(LocalDate.class), anyInt());
    }

    @Test
    @DisplayName("Doit mettre à jour une course avec succès")
    void shouldUpdateCourseSuccessfully() throws Exception {
        // Given
        UpdateCourseRequest request = new UpdateCourseRequest("Course Mise à Jour", LocalDate.of(2025, 7, 7), 3);

        when(courseManagementUseCase.getCourseById(new CourseId(1L))).thenReturn(testCourse);

        // When & Then
        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(courseManagementUseCase).updateCourse(
                new CourseId(1L), request.nom(), request.date(), request.numero());
        verify(courseManagementUseCase).getCourseById(new CourseId(1L));
    }

    @Test
    @DisplayName("Doit supprimer une course avec succès")
    void shouldDeleteCourseSuccessfully() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());

        verify(courseManagementUseCase).deleteCourse(new CourseId(1L));
    }

    @Test
    @DisplayName("Doit récupérer une course par son ID")
    void shouldGetCourseById() throws Exception {
        // Given
        when(courseManagementUseCase.getCourseById(new CourseId(1L))).thenReturn(testCourse);

        // When & Then
        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Course Test"))
                .andExpect(jsonPath("$.date").value("2025-05-05"))
                .andExpect(jsonPath("$.numero").value(1));

        verify(courseManagementUseCase).getCourseById(new CourseId(1L));
    }

    @Test
    @DisplayName("Doit retourner une erreur quand la course n'existe pas")
    void shouldReturnErrorWhenCourseDoesNotExist() throws Exception {
        // Given
        when(courseManagementUseCase.getCourseById(new CourseId(999L)))
                .thenThrow(new CourseInexistanteException("Course non trouvée avec l'id : 999"));

        // When & Then
        mockMvc.perform(get("/api/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("COURSE_NOT_FOUND"));

        verify(courseManagementUseCase).getCourseById(new CourseId(999L));
    }

    @Test
    @DisplayName("Doit récupérer toutes les courses")
    void shouldGetAllCourses() throws Exception {
        // Given
        when(courseManagementUseCase.getAllCourses()).thenReturn(Arrays.asList(testCourse));

        // When & Then
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("Course Test"));

        verify(courseManagementUseCase).getAllCourses();
    }

    @Test
    @DisplayName("Doit ajouter un participant à une course")
    void shouldAddParticipantToCourse() throws Exception {
        // Given
        CreateParticipantRequest request = new CreateParticipantRequest("Doe");

        ParticipantId participantId = new ParticipantId(99L);
        when(participantManagementUseCase.addParticipant(
                eq(new CourseId(1L)), anyString()))
                .thenReturn(participantId);

        when(participantManagementUseCase.getParticipantById(participantId)).thenReturn(testParticipant);

        // When & Then
        mockMvc.perform(post("/api/courses/1/participants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Doe"))
                .andExpect(jsonPath("$.dossard").value(42));

        verify(participantManagementUseCase).addParticipant(
                new CourseId(1L), request.nom());
        verify(participantManagementUseCase).getParticipantById(participantId);
    }
}

