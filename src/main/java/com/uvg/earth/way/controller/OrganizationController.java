package com.uvg.earth.way.controller;

import com.uvg.earth.way.dto.OrganizationRequestDto;
import com.uvg.earth.way.dto.OrganizationResponseDto;
import com.uvg.earth.way.service.OrganizationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
@RequestMapping(value = "/api/v1/organization")
@RestController
public class OrganizationController {
    private final OrganizationService organizationService;

    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private final static String ADMIN = "ADMIN";
    private final static String USER = "USER";
    private final static String ORGANIZATION = "ORGANIZATION";
    /**
     * Obtener todas las organizaciones con paginación
     * GET /api/v1/organization?page=0&size=10
     */
    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("")
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
    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("/{id}")
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
    @RolesAllowed({ADMIN})
    @PostMapping( "")
    public ResponseEntity<Map<String, Object>> createOrganization(
            @Valid @RequestBody OrganizationRequestDto requestDto) {

        Map<String, Object> response = new HashMap<>();

        try {
            OrganizationResponseDto createdOrganization = organizationService.createOrganization(requestDto);

            response.put("payload", createdOrganization);
            response.put(MESSAGE, "Organización creada con éxito");

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
    @RolesAllowed({ADMIN})
    @PutMapping("/{id}")
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
    @RolesAllowed({ADMIN})
    @DeleteMapping("/{id}")
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
    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("/search")
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
