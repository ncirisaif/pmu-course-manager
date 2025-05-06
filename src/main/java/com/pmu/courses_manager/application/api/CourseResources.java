package com.pmu.courses_manager.application.api;


import com.pmu.courses_manager.application.api.dto.CourseDto;
import com.pmu.courses_manager.application.api.dto.ParticipantDto;
import com.pmu.courses_manager.application.api.request.CreateCourseRequest;
import com.pmu.courses_manager.application.api.request.CreateParticipantRequest;
import com.pmu.courses_manager.application.api.request.UpdateCourseRequest;
import com.pmu.courses_manager.application.mapper.ApplicationMapper;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Participant;
import com.pmu.courses_manager.domain.model.ParticipantId;
import com.pmu.courses_manager.domain.port.in.CourseManagementUseCase;
import com.pmu.courses_manager.domain.port.in.ParticipantManagementUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour la gestion des courses
 * Implémente l'interface CourseApi pour la documentation OpenAPI
 */
@RestController
@RequestMapping("/api/courses")
public class CourseResources implements CourseResourcesApi {

    private final CourseManagementUseCase courseManagementUseCase;
    private final ParticipantManagementUseCase participantManagementUseCase;
    private final ApplicationMapper mapper;

    public CourseResources(
            CourseManagementUseCase courseManagementUseCase,
            ParticipantManagementUseCase participantManagementUseCase, ApplicationMapper mapper) {
        this.courseManagementUseCase = courseManagementUseCase;
        this.participantManagementUseCase = participantManagementUseCase;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        CourseId courseId = courseManagementUseCase.createCourse(
                request.nom(),
                request.date(),
                request.numero());

        Course course = courseManagementUseCase.getCourseById(courseId);
        return new ResponseEntity<>(mapper.mapToCourseDto(course), HttpStatus.CREATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request) {

        courseManagementUseCase.updateCourse(
                new CourseId(id),
                request.nom(),
                request.date(),
                request.numero());

        Course course = courseManagementUseCase.getCourseById(new CourseId(id));
        return ResponseEntity.ok(mapper.mapToCourseDto(course));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseManagementUseCase.deleteCourse(new CourseId(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        Course course = courseManagementUseCase.getCourseById(new CourseId(id));
        return ResponseEntity.ok(mapper.mapToCourseDto(course));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<Course> courses = courseManagementUseCase.getAllCourses();
        List<CourseDto> courseDtos = courses.stream()
                .map(mapper::mapToCourseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(courseDtos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping("/{courseId}/participants")
    public ResponseEntity<ParticipantDto> addParticipant(
            @PathVariable Long courseId,
            @Valid @RequestBody CreateParticipantRequest request) {

        ParticipantId participantId = participantManagementUseCase.addParticipant(
                new CourseId(courseId),
                request.nom());

        Participant participant = participantManagementUseCase.getParticipantById(participantId);
        return new ResponseEntity<>(mapper.mapToParticipantDto(participant), HttpStatus.CREATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping("/{courseId}/participants")
    public ResponseEntity<List<ParticipantDto>> getParticipantsByCourse(@PathVariable Long courseId) {
        List<Participant> participants = participantManagementUseCase.getParticipantsByCourse(new CourseId(courseId));
        List<ParticipantDto> participantDtos = participants.stream()
                .map(mapper::mapToParticipantDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(participantDtos);
    }

}