package com.pmu.courses_manager.infrastructure.adapter.messaging;

public class ParticipantAddedEvent {
    private Long courseId;
    private Long participantId;
    private String nom;
    private Integer dossard;

    public ParticipantAddedEvent() {}

    public ParticipantAddedEvent(Long courseId, Long participantId, String nom, Integer dossard) {
        this.courseId = courseId;
        this.participantId = participantId;
        this.nom = nom;
        this.dossard = dossard;
    }

    // Getters et setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getDossard() { return dossard; }
    public void setDossard(Integer dossard) { this.dossard = dossard; }
}
