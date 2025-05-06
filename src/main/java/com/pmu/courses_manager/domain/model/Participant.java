package com.pmu.courses_manager.domain.model;

import java.util.Objects;

/**
 * Entité Participant du domaine
 */
public class Participant {
    private ParticipantId id;
    private String nom;
    private Integer dossard;

    public Participant() {
    }

    private Participant(String nom, Integer dossard) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du participant ne peut pas être vide");
        }
        this.nom = nom;
        this.dossard = dossard;
    }

    public static Participant create(String nom, Integer dossard) {
        return new Participant(nom, dossard);
    }

    public static Participant reconstitute(ParticipantId id, String nom, Integer dossard) {
        Participant participant = new Participant(nom, dossard);
        participant.id = id;
        return participant;
    }

    public void updateDetails(String nom, Integer dossard) {
        if (nom != null && !nom.trim().isEmpty()) {
            this.nom = nom;
        }
        if (dossard != null) {
            this.dossard = dossard;
        }
    }

    public ParticipantId getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Integer getDossard() {
        return dossard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(nom, that.nom) &&
                Objects.equals(dossard, that.dossard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, dossard);
    }
    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dossard=" + dossard +
                '}';
    }
}