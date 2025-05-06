package com.pmu.courses_manager.application.mapper;

import com.pmu.courses_manager.application.api.dto.CourseDto;
import com.pmu.courses_manager.application.api.dto.ParticipantDto;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.Participant;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {
    public ApplicationMapper(){}
    public ParticipantDto mapToParticipantDto(Participant participant) {
        return new ParticipantDto(
                participant.getNom(),
                participant.getDossard()
        );
    }
    public CourseDto mapToCourseDto(Course course) {
        return new CourseDto(
                course.getId().getValue(),
                course.getNom(),
                course.getDate(),
                course.getNumero()
        );
    }
}