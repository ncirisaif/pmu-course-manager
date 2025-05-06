package com.pmu.courses_manager.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmu.courses_manager.application.api.CourseResources;
import com.pmu.courses_manager.application.api.request.CreateCourseRequest;
import com.pmu.courses_manager.application.exception.CourseExisteDejaException;
import com.pmu.courses_manager.application.mapper.ApplicationMapper;
import com.pmu.courses_manager.domain.port.in.CourseManagementUseCase;
import com.pmu.courses_manager.domain.port.in.ParticipantManagementUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour gérer les exceptions du contrôleur
 */
@WebMvcTest(CourseResources.class)
public class CourseControllerExceptionHandlingTest {

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

    @Test
    @DisplayName("Doit gérer l'exception CourseExisteDejaException")
    void shouldHandleCourseExisteDejaException() throws Exception {
        // Given
        CreateCourseRequest request = new CreateCourseRequest("Course Test", LocalDate.of(2025, 5, 5), 1);

        when(courseManagementUseCase.createCourse(anyString(), any(LocalDate.class), anyInt()))
                .thenThrow(new CourseExisteDejaException("Une course avec cette date et ce numéro existe déjà"));

        // When & Then
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("COURSE_DUPLICATED"))
                .andExpect(jsonPath("$.message").value("Une course avec cette date et ce numéro existe déjà"));
    }
}
