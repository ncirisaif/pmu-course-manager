package com.pmu.courses_manager.unit;

import com.pmu.courses_manager.application.exception.CourseExisteDejaException;
import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.port.out.CourseEventPort;
import com.pmu.courses_manager.domain.port.out.CoursePersistencePort;
import com.pmu.courses_manager.domain.port.out.OutboxEventPersistencePort;
import com.pmu.courses_manager.domain.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le service CourseService
 */
@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CoursePersistencePort coursePersistencePort;

    @Mock
    private CourseEventPort courseEventPort;

    @Mock
    private OutboxEventPersistencePort outboxEventPersistencePort;

    private CourseService courseService;

    private static final String NOM_VALIDE = "Course de Test";
    private static final LocalDate DATE_VALIDE = LocalDate.of(2025, 5, 5);
    private static final Integer NUMERO_VALIDE = 1;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(coursePersistencePort, courseEventPort, outboxEventPersistencePort);
    }

    @Nested
    @DisplayName("Tests de création de course")
    class CreateCourseTests {

        @Test
        @DisplayName("Doit créer une course avec succès")
        void shouldCreateCourseSuccessfully() {
            // Given
            when(coursePersistencePort.existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE)).thenReturn(false);

            Course createdCourse = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);
            try {
                java.lang.reflect.Field idField = Course.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(createdCourse, new CourseId(1L));
                idField.setAccessible(false);
            } catch (Exception e) {
                fail("Erreur lors de la configuration du test");
            }

            when(coursePersistencePort.save(any(Course.class))).thenReturn(createdCourse);

            // When
            CourseId courseId = courseService.createCourse(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);

            // Then
            assertNotNull(courseId);
            assertEquals(1L, courseId.getValue());

            // Vérification des interactions
            verify(coursePersistencePort).existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE);

            ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
            verify(coursePersistencePort).save(courseCaptor.capture());

            Course capturedCourse = courseCaptor.getValue();
            assertEquals(NOM_VALIDE, capturedCourse.getNom());
            assertEquals(DATE_VALIDE, capturedCourse.getDate());
            assertEquals(NUMERO_VALIDE, capturedCourse.getNumero());
        }

        @Test
        @DisplayName("Doit rejeter une course avec date et numéro déjà existants")
        void shouldRejectCourseWithExistingDateAndNumero() {
            // Given
            when(coursePersistencePort.existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE)).thenReturn(true);

            // When & Then
            assertThrows(CourseExisteDejaException.class, () -> {
                courseService.createCourse(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);
            });

            verify(coursePersistencePort).existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE);
            verify(coursePersistencePort, never()).save(any(Course.class));
            verify(courseEventPort, never()).publishCourseCreated(any(Course.class));
        }
    }

    @Nested
    @DisplayName("Tests de mise à jour de course")
    class UpdateCourseTests {

        @Test
        @DisplayName("Doit rejeter la mise à jour si la course n'existe pas")
        void shouldRejectUpdateIfCourseDoesNotExist() {
            // Given
            CourseId courseId = new CourseId(999L);
            when(coursePersistencePort.findById(courseId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CourseInexistanteException.class, () -> {
                courseService.updateCourse(courseId, "Nouveau nom", LocalDate.now(), 2);
            });

            verify(coursePersistencePort).findById(courseId);
            verify(coursePersistencePort, never()).save(any(Course.class));
        }
    }

    @Nested
    @DisplayName("Tests de suppression de course")
    class DeleteCourseTests {

        @Test
        @DisplayName("Doit supprimer une course avec succès")
        void shouldDeleteCourseSuccessfully() {
            // Given
            CourseId courseId = new CourseId(1L);
            when(coursePersistencePort.findById(courseId)).thenReturn(Optional.of(mock(Course.class)));

            // When
            courseService.deleteCourse(courseId);

            // Then
            verify(coursePersistencePort).delete(courseId);
        }

        @Test
        @DisplayName("Doit rejeter la suppression si la course n'existe pas")
        void shouldRejectDeleteIfCourseDoesNotExist() {
            // Given
            CourseId courseId = new CourseId(999L);
            when(coursePersistencePort.findById(courseId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CourseInexistanteException.class, () -> {
                courseService.deleteCourse(courseId);
            });

            verify(coursePersistencePort, never()).delete(any(CourseId.class));
        }
    }

    @Nested
    @DisplayName("Tests des requêtes sur les courses")
    class CourseQueryTests {

        @Test
        @DisplayName("Doit récupérer une course par son ID")
        void shouldGetCourseById() {
            // Given
            CourseId courseId = new CourseId(1L);
            Course course = mock(Course.class);
            when(coursePersistencePort.findById(courseId)).thenReturn(Optional.of(course));

            // When
            Course result = courseService.getCourseById(courseId);

            // Then
            assertNotNull(result);
            assertEquals(course, result);
        }

        @Test
        @DisplayName("Doit lever une exception si la course n'existe pas")
        void shouldThrowExceptionIfCourseNotFound() {
            // Given
            CourseId courseId = new CourseId(999L);
            when(coursePersistencePort.findById(courseId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(CourseInexistanteException.class, () -> {
                courseService.getCourseById(courseId);
            });
        }
    }
}

