package com.uvg.earth.way.controller;

import com.uvg.earth.way.dto.OrganizationRequestDto;
import com.uvg.earth.way.dto.OrganizationResponseDto;
import com.uvg.earth.way.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/organization")
@RequiredArgsConstructor
@CrossOrigin
public class OrganizationController {
    private final OrganizationService organizationService;

    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    /**
     * Obtener todas las organizaciones con paginación
     * GET /api/v1/organization?page=0&size=10
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "")
    public ResponseEntity<Map<String, Object>> getAllOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Map<String, Object> response = new HashMap<>();

        try {
            Pageable organizationPage = PageRequest.of(page, size);
            Page<OrganizationResponseDto> organizations = organizationService.getAllOrganizations(organizationPage);

            response.put("payload", organizations.getContent());
            response.put(MESSAGE, "Organizations retrieved successfully");
            response.put("totalPages", organizations.getTotalPages());
            response.put("totalElements", organizations.getTotalElements());
            response.put("currentPage", organizations.getNumber());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener organización por ID
     * GET /api/v1/organization/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> getOrganizationById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<OrganizationResponseDto> organization = organizationService.getOrganizationById(id);

            if (organization.isPresent()) {
                response.put("payload", organization.get());
                response.put(MESSAGE, "Organization found successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put(MESSAGE, "Organization not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Crear nueva organización
     * POST /api/v1/organization
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<Map<String, Object>> createOrganization(
            @Valid @RequestBody OrganizationRequestDto requestDto,
            @RequestParam Long creatorId) {

        Map<String, Object> response = new HashMap<>();

        try {
            OrganizationResponseDto createdOrganization = organizationService.createOrganization(requestDto, creatorId);

            response.put("payload", createdOrganization);
            response.put(MESSAGE, "Organization created successfully");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            response.put(MESSAGE, ERROR);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "Internal server error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar organización existente
     * PUT /api/v1/organization/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequestDto requestDto) {

        Map<String, Object> response = new HashMap<>();

        try {
            OrganizationResponseDto updatedOrganization = organizationService.updateOrganization(id, requestDto);

            response.put("payload", updatedOrganization);
            response.put(MESSAGE, "Organization updated successfully");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put(MESSAGE, ERROR);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "Internal server error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar organización
     * DELETE /api/v1/organization/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrganization(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            organizationService.deleteOrganization(id);

            response.put(MESSAGE, "Organization deleted successfully");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put(MESSAGE, ERROR);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "Internal server error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar organización por nombre
     * GET /api/v1/organization/search?name=nombreOrganizacion
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/search")
    public ResponseEntity<Map<String, Object>> getOrganizationByName(
            @RequestParam String name) {

        Map<String, Object> response = new HashMap<>();

        try {
            Optional<OrganizationResponseDto> organization = organizationService.getOrganizationByName(name);

            if (organization.isPresent()) {
                response.put("payload", organization.get());
                response.put(MESSAGE, "Organization found successfully");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
