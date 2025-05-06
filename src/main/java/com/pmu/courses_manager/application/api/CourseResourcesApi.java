package com.pmu.courses_manager.application.api;


import com.pmu.courses_manager.application.api.dto.CourseDto;
import com.pmu.courses_manager.application.api.dto.ParticipantDto;
import com.pmu.courses_manager.application.api.request.CreateCourseRequest;
import com.pmu.courses_manager.application.api.request.CreateParticipantRequest;
import com.pmu.courses_manager.application.api.request.UpdateCourseRequest;
import com.pmu.courses_manager.application.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Interface de documentation pour l'API de gestion des courses
 */
@Tag(name = "Courses", description = "API pour la gestion des courses sportives et des participants")
public interface CourseResourcesApi {

    /**
     * Crée une nouvelle course
     */
    @Operation(
            summary = "Crée une nouvelle course",
            description = "Crée une nouvelle course avec un nom, une date et un numéro. " +
                    "Le numéro doit être unique pour la date donnée."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Course créée avec succès",
                    content = @Content(schema = @Schema(implementation = CourseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de requête invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Une course avec cette date et ce numéro existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CreateCourseRequest request);

    /**
     * Met à jour une course existante
     */
    @Operation(
            summary = "Met à jour une course existante",
            description = "Met à jour le nom, la date et/ou le numéro d'une course existante. " +
                    "Si la date ou le numéro sont modifiés, la nouvelle combinaison date/numéro doit être unique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course mise à jour avec succès",
                    content = @Content(schema = @Schema(implementation = CourseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de requête invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Une course avec cette date et ce numéro existe déjà",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<CourseDto> updateCourse(
            @Parameter(description = "ID de la course à mettre à jour") @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request);

    /**
     * Supprime une course
     */
    @Operation(
            summary = "Supprime une course",
            description = "Supprime une course et tous ses participants associés."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Course supprimée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<Void> deleteCourse(
            @Parameter(description = "ID de la course à supprimer") @PathVariable Long id);

    /**
     * Récupère une course par son identifiant
     */
    @Operation(
            summary = "Récupère une course par son ID",
            description = "Renvoie les détails d'une course spécifique identifiée par son ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Course récupérée avec succès",
                    content = @Content(schema = @Schema(implementation = CourseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<CourseDto> getCourseById(
            @Parameter(description = "ID de la course à récupérer") @PathVariable Long id);

    /**
     * Liste toutes les courses
     */
    @Operation(
            summary = "Liste toutes les courses",
            description = "Récupère la liste de toutes les courses disponibles."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des courses récupérée avec succès",
                    content = @Content(schema = @Schema(implementation = CourseDto.class))
            )
    })
    ResponseEntity<List<CourseDto>> getAllCourses();

    /**
     * Ajoute un participant à une course
     */
    @Operation(
            summary = "Ajoute un participant à une course",
            description = "Ajoute un nouveau participant à une course existante. " +
                    "Le dossard doit être unique dans la course."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Participant ajouté avec succès",
                    content = @Content(schema = @Schema(implementation = ParticipantDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de requête invalides",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Dossard déjà utilisé dans cette course",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<ParticipantDto> addParticipant(
            @Parameter(description = "ID de la course") @PathVariable Long courseId,
            @Valid @RequestBody CreateParticipantRequest request);

    /**
     * Liste les participants d'une course
     */
    @Operation(
            summary = "Liste les participants d'une course",
            description = "Récupère la liste des participants d'une course spécifique."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des participants récupérée avec succès",
                    content = @Content(schema = @Schema(implementation = ParticipantDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course non trouvée",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<List<ParticipantDto>> getParticipantsByCourse(
            @Parameter(description = "ID de la course") @PathVariable Long courseId);
}