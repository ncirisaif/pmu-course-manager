package com.pmu.courses_manager.infrastructure.adapter.persistence.entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Entité JPA pour Course
 */
@Entity
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(name = "uk_date_numero", columnNames = {"date_course", "numero"})
})
public class CourseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "date_course", nullable = false)
    private LocalDate date;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParticipantJpaEntity> participants = new HashSet<>();

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Set<ParticipantJpaEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ParticipantJpaEntity> participants) {
        this.participants = participants;
    }

    public void addParticipant(ParticipantJpaEntity participant) {
        participants.add(participant);
        participant.setCourse(this);
    }
}
