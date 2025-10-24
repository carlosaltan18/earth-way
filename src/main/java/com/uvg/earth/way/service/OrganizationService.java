package com.uvg.earth.way.service;
import com.uvg.earth.way.dto.OrganizationRequestDto;
import com.uvg.earth.way.dto.OrganizationResponseDto;
import com.uvg.earth.way.dto.UserDto;
import com.uvg.earth.way.model.Organization;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.OrganizationRepository;
import com.uvg.earth.way.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<OrganizationResponseDto> getAllOrganizations(Pageable pageable) {
        Page<Organization> organizations = organizationRepository.findAll(pageable);
        return organizations.map(this::convertToResponseDto);
    }
    /**
     * Obtener organización por ID
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationResponseDto> getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public Optional<Organization> findEntityById(Long id) {
        return organizationRepository.findById(id);
    }

    /**
     * Crear nueva organización
     */
    public OrganizationResponseDto createOrganization(OrganizationRequestDto requestDto) {
        // Buscar el usuario creator
        Long creatorId = requestDto.getCreatorId();
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + creatorId));

        // Verificar que no exista una organización con el mismo nombre
        if (organizationRepository.findByName(requestDto.getName()).isPresent()) {
            throw new RuntimeException("Organización con nombre: '" + requestDto.getName() + "' ya existe");
        }

        // Crear la organización
        Organization organization = new Organization();
        organization.setName(requestDto.getName());
        organization.setDescription(requestDto.getDescription());
        organization.setContactEmail(requestDto.getContactEmail());
        organization.setContactPhone(requestDto.getContactPhone());
        organization.setLogo(requestDto.getLogo());
        organization.setCreator(creator);

        // Guardar en la base de datos
        Organization savedOrganization = organizationRepository.save(organization);

        return convertToResponseDto(savedOrganization);
    }

    /**
     * Actualizar organización existente
     */
    public OrganizationResponseDto updateOrganization(Long id, OrganizationRequestDto requestDto) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));

        // Verificar que no exista otra organización con el mismo nombre (excepto la actual)
        Optional<Organization> existingOrg = organizationRepository.findByName(requestDto.getName());
        if (existingOrg.isPresent() && !existingOrg.get().getId().equals(id)) {
            throw new RuntimeException("Organización con el nombre: '" + requestDto.getName() + "' ya existe");
        }

        // Actualizar campos
        organization.setName(requestDto.getName());
        organization.setDescription(requestDto.getDescription());
        organization.setContactEmail(requestDto.getContactEmail());
        organization.setContactPhone(requestDto.getContactPhone());
        organization.setLogo(requestDto.getLogo());

        // Guardar cambios
        Organization updatedOrganization = organizationRepository.save(organization);

        return convertToResponseDto(updatedOrganization);
    }

    /**
     * Eliminar organización
     */
    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new RuntimeException("Organización no encontrada con ID: " + id);
        }
        organizationRepository.deleteById(id);
    }

    /**
     * Buscar organizaciones por nombre
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationResponseDto> getOrganizationByName(String name) {
        return organizationRepository.findByName(name)
                .map(this::convertToResponseDto);
    }

    /**
     * Convertir Organization entity a OrganizationResponseDto
     */
    private OrganizationResponseDto convertToResponseDto(Organization organization) {
        UserDto creatorDto = null;
        if (organization.getCreator() != null) {
            User creator = organization.getCreator();
            creatorDto = new UserDto(
                    creator.getName(),
                    creator.getSurname(),
                    creator.getEmail(),
                    creator.getPhone()
            );
        }

        return new OrganizationResponseDto(
                organization.getId(),
                organization.getName(),
                organization.getDescription(),
                organization.getContactEmail(),
                organization.getContactPhone(),
                organization.getLogo(),
                creatorDto
        );
    }
}

