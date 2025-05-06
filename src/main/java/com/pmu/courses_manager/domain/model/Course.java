package com.pmu.courses_manager.domain.model;

import com.pmu.courses_manager.application.exception.DossardDejaUtiliseException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Agrégat racine représentant une course.
 * Une course a lieu un jour donné et possède un nom et un numéro unique pour ce jour.
 */
public class Course {
    private CourseId id;
    private String nom;
    private LocalDate date;
    private Integer numero;
    private final Set<Participant> participants = new HashSet<>();

    public Course() {
    }
    protected Course(CourseId id, String nom, LocalDate date, Integer numero) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la course ne peut pas être vide");
        }
        if (date == null) {
            throw new IllegalArgumentException("La date de la course est obligatoire");
        }
        if (numero == null || numero <= 0) {
            throw new IllegalArgumentException("Le numéro de la course doit être un entier positif");
        }
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.numero = numero;
    }

    public static Course create(String nom, LocalDate date, Integer numero) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la course ne peut pas être vide");
        }
        if (date == null) {
            throw new IllegalArgumentException("La date de la course est obligatoire");
        }
        if (numero == null || numero <= 0) {
            throw new IllegalArgumentException("Le numéro de la course doit être un entier positif");
        }

        Course course = new Course();
        course.nom = nom;
        course.date = date;
        course.numero = numero;
        return course;
    }
    public static Course reconstitute(CourseId id, String nom, LocalDate date, Integer numero) {
        Course course = new Course();
        course.id = id;
        course.nom = nom;
        course.date = date;
        course.numero = numero;
        return course;
    }
    public void updateDetails(String nom, LocalDate date, Integer numero) {
        if (nom != null && !nom.trim().isEmpty()) {
            this.nom = nom;
        }
        if (date != null) {
            this.date = date;
        }
        if (numero != null && numero > 0) {
            this.numero = numero;
        }
    }

    public Participant addParticipant(String nom, Integer dossard) {
        Participant participant = Participant.create(nom, dossard);
        participants.add(participant);
        return participant;
    }

    public void removeParticipant(ParticipantId participantId) {
        participants.removeIf(p -> p.getId().equals(participantId));
    }

    public Integer getNextAvailableDossard() {
        return participants.stream()
                .map(Participant::getDossard)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(0) + 1;
    }

    public CourseId getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getNumero() {
        return numero;
    }

    public Set<Participant> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(date, course.date) &&
                Objects.equals(numero, course.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, numero);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                ", numero=" + numero +
                '}';
    }
}