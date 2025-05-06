package com.pmu.courses_manager.domain.port.out;

import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.OutboxEvent;

public interface OutboxEventPersistencePort {
    OutboxEvent saveAddedParticipantEvent(CourseId courseId, Participant participant);
    OutboxEvent saveCreatedCourseEvent(Course course);
}
