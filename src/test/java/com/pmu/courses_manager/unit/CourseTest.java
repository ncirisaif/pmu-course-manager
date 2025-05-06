package com.pmu.courses_manager.unit;


import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le modèle de domaine Course
 */
public class CourseTest {

    private static final String NOM_VALIDE = "Course de Test";
    private static final LocalDate DATE_VALIDE = LocalDate.of(2025, 5, 5);
    private static final Integer NUMERO_VALIDE = 1;

    @Nested
    @DisplayName("Tests de création de Course")
    class CreateCourseTests {

        @Test
        @DisplayName("Doit créer une course valide")
        void shouldCreateValidCourse() {
            // When
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);

            // Then
            assertNotNull(course);
            assertEquals(NOM_VALIDE, course.getNom());
            assertEquals(DATE_VALIDE, course.getDate());
            assertEquals(NUMERO_VALIDE, course.getNumero());
            assertTrue(course.getParticipants().isEmpty());
        }

        @Test
        @DisplayName("Doit rejeter un nom vide")
        void shouldRejectEmptyName() {
            // Then
            assertThrows(IllegalArgumentException.class, () -> {
                Course.create("", DATE_VALIDE, NUMERO_VALIDE);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                Course.create(null, DATE_VALIDE, NUMERO_VALIDE);
            });
        }

        @Test
        @DisplayName("Doit rejeter une date nulle")
        void shouldRejectNullDate() {
            // Then
            assertThrows(IllegalArgumentException.class, () -> {
                Course.create(NOM_VALIDE, null, NUMERO_VALIDE);
            });
        }

        @Test
        @DisplayName("Doit rejeter un numéro invalide")
        void shouldRejectInvalidNumero() {
            // Then
            assertThrows(IllegalArgumentException.class, () -> {
                Course.create(NOM_VALIDE, DATE_VALIDE, null);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                Course.create(NOM_VALIDE, DATE_VALIDE, 0);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                Course.create(NOM_VALIDE, DATE_VALIDE, -1);
            });
        }
    }

    @Nested
    @DisplayName("Tests de mise à jour de Course")
    class UpdateCourseTests {

        @Test
        @DisplayName("Doit mettre à jour les détails de la course")
        void shouldUpdateCourseDetails() {
            // Given
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);
            String nouveauNom = "Nouvelle Course";
            LocalDate nouvelleDate = LocalDate.of(2025, 6, 6);
            Integer nouveauNumero = 2;

            // When
            course.updateDetails(nouveauNom, nouvelleDate, nouveauNumero);

            // Then
            assertEquals(nouveauNom, course.getNom());
            assertEquals(nouvelleDate, course.getDate());
            assertEquals(nouveauNumero, course.getNumero());
        }

        @Test
        @DisplayName("Ne doit pas mettre à jour avec des valeurs nulles")
        void shouldNotUpdateWithNullValues() {
            // Given
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);

            // When
            course.updateDetails(null, null, null);

            // Then
            assertEquals(NOM_VALIDE, course.getNom());
            assertEquals(DATE_VALIDE, course.getDate());
            assertEquals(NUMERO_VALIDE, course.getNumero());
        }

        @Test
        @DisplayName("Ne doit pas mettre à jour avec un nom vide")
        void shouldNotUpdateWithEmptyName() {
            // Given
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);

            // When
            course.updateDetails("", DATE_VALIDE, NUMERO_VALIDE);

            // Then
            assertEquals(NOM_VALIDE, course.getNom());
        }

        @Test
        @DisplayName("Ne doit pas mettre à jour avec un numéro invalide")
        void shouldNotUpdateWithInvalidNumero() {
            // Given
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);

            // When
            course.updateDetails(NOM_VALIDE, DATE_VALIDE, 0);

            // Then
            assertEquals(NUMERO_VALIDE, course.getNumero());

            // When
            course.updateDetails(NOM_VALIDE, DATE_VALIDE, -1);

            // Then
            assertEquals(NUMERO_VALIDE, course.getNumero());
        }
    }

    @Nested
    @DisplayName("Tests de gestion des participants")
    class ParticipantManagementTests {

        @Test
        @DisplayName("Doit ajouter un participant valide")
        void shouldAddValidParticipant() {
            // Given
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);
            String nom = "Doe";
            String prenom = "John";
            Integer dossard = 42;

            // When
            Participant participant = course.addParticipant(nom, dossard);

            // Then
            assertNotNull(participant);
            assertEquals(nom, participant.getNom());
            assertEquals(dossard, participant.getDossard());
            assertEquals(1, course.getParticipants().size());
            assertTrue(course.getParticipants().contains(participant));
        }

        @Test
        @DisplayName("Doit supprimer un participant")
        void shouldRemoveParticipant() {
            // Given
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);
            Participant participant = course.addParticipant("Doe", 42);

            // When - simuler l'ID du participant pour le test
            try {
                java.lang.reflect.Field idField = Participant.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(participant, new ParticipantId(1L));
                idField.setAccessible(false);

                course.removeParticipant(participant.getId());

                // Then
                assertEquals(0, course.getParticipants().size());
            } catch (Exception e) {
                fail("Exception lors de la configuration du test: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Doit retourner 1 comme premier dossard si aucun participant")
        void shouldReturnOneAsFirstDossardWhenNoParticipants() {
            // Given
            Course course = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);

            // When
            Integer nextDossard = course.getNextAvailableDossard();

            // Then
            assertEquals(1, nextDossard);
        }
    }

    @Nested
    @DisplayName("Tests d'égalité et hashCode")
    class EqualityTests {

        @Test
        @DisplayName("Deux courses avec même date et numéro doivent être égales")
        void coursesWithSameDateAndNumeroShouldBeEqual() {
            // Given
            Course course1 = Course.create("Course 1", DATE_VALIDE, NUMERO_VALIDE);
            Course course2 = Course.create("Course 2", DATE_VALIDE, NUMERO_VALIDE);

            // Then
            assertEquals(course1, course2);
            assertEquals(course1.hashCode(), course2.hashCode());
        }

        @Test
        @DisplayName("Deux courses avec date ou numéro différents ne doivent pas être égales")
        void coursesWithDifferentDateOrNumeroShouldNotBeEqual() {
            // Given
            Course course1 = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE);
            Course course2 = Course.create(NOM_VALIDE, DATE_VALIDE.plusDays(1), NUMERO_VALIDE);
            Course course3 = Course.create(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE + 1);

            // Then
            assertNotEquals(course1, course2);
            assertNotEquals(course1, course3);
            assertNotEquals(course2, course3);
        }
    }
}
