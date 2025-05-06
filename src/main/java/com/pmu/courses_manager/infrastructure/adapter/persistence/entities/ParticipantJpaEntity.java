package com.pmu.courses_manager.infrastructure.adapter.persistence.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité JPA pour Participant
 */
@Entity
@Table(name = "participants", uniqueConstraints = {
        @UniqueConstraint(name = "uk_course_dossard", columnNames = {"course_id", "dossard"})
})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParticipantJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "dossard", nullable = false)
    private Integer dossard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseJpaEntity course;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getDossard() {
        return dossard;
    }

    public void setDossard(Integer dossard) {
        this.dossard = dossard;
    }

    public CourseJpaEntity getCourse() {
        return course;
    }

    public void setCourse(CourseJpaEntity course) {
        this.course = course;
    }
}