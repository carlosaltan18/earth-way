package com.uvg.earth.way.service;

import com.uvg.earth.way.dto.EventDto;
import com.uvg.earth.way.dto.OrganizationResponseDto;
import com.uvg.earth.way.dto.UserDto;
import com.uvg.earth.way.dto.UserEventDto;
import com.uvg.earth.way.exception.EventDeletionException;
import com.uvg.earth.way.model.Event;
import com.uvg.earth.way.model.Organization;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.EventRepository;
import com.uvg.earth.way.service.interfaces.IEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService implements IEventService {

    private EventRepository eventRepository;
    private final UserService userService;
    private final OrganizationService organizationService;
    private final GeometryFactory geometryFactory;
    /**
     * Finds an event by ID
     * @param id
     * @return Event or null if not found
     */
    @Override
    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    /**
     * Saves an event
     * @param event
     */
    @Override
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    /**
     * Deletes an event by ID
     * @param idEvent
     */
    @Override
    public void deleteEvent(Long idEvent) {
        if (!eventRepository.existsById(idEvent)) {
            throw new IllegalArgumentException("No se encontró el evento con el id: " + idEvent);
        }
        try {
            eventRepository.deleteById(idEvent);
        } catch (DataAccessException e) {
            throw new EventDeletionException("Error eliminando el evento con id: " + idEvent, e);
        }
    }

    /**
     * Updates an event with the provided DTO
     * @param eventDto
     * @param idEvent
     */
    @Override
    public void updateEvent(EventDto eventDto, Long idEvent) {
        validateEventDto(idEvent);
        Optional<Event> eventOptional = eventRepository.findById(idEvent);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();

            // Updating basic fields
            if (eventDto.getName() != null && !eventDto.getName().trim().isEmpty()) {
                event.setName(eventDto.getName());
            }
            if (eventDto.getDescription() != null && !eventDto.getDescription().trim().isEmpty()) {
                event.setDescription(eventDto.getDescription());
            }
            if (eventDto.getDirection() != null && !eventDto.getDirection().trim().isEmpty()) {
                event.setDirection(eventDto.getDirection());
            }
            if (eventDto.getDate() != null) {
                event.setDate(eventDto.getDate());
            }
            /**
            if (eventDto.getLatitude() != null && eventDto.getLongitude() != null) {
                Point point = geometryFactory.createPoint(
                        new Coordinate(eventDto.getLongitude(), eventDto.getLatitude())
                );
                point.setSRID(4326);
                event.setLocation(point);
            }
                **/
            event.setFinished(eventDto.isFinished());

            // Update organizer
            addingOrganizer(eventDto, event);

            // Update organization if provided
           // updateOrganization(eventDto, event);

            // Update participants if provided
            //updateParticipants(eventDto, event);

            eventRepository.save(event);
        } else {
            throw new EntityNotFoundException("Evento con id " + idEvent + " no encontrado");
        }
    }

    /**
     * Validates if an event exists
     * @param idEvent
     */
    public void validateEventDto(Long idEvent) {
        if (!eventRepository.existsById(idEvent)) {
            throw new EntityNotFoundException("Evento " + idEvent + " no existe");
        }
    }

    /**
     * Adds or updates organizer for an event
     * @param eventDto
     * @param event
     */
    public void addingOrganizer(EventDto eventDto, Event event) {
        if (eventDto.getIdOrganizer() != null) {
            User user = userService.findById(eventDto.getIdOrganizer())
                    .orElseThrow(() -> new EntityNotFoundException("Organizador no encontrado."));

            event.setOrganizer(user);
        }
    }

    /**
     * Updates organization for an event
     * @param eventDto
     * @param event
     */
   /* public void updateOrganization(EventDto eventDto, Event event) {
        if (eventDto.getIdOrganization() != null) {
            Organization organization = organizationService.findById(eventDto.getIdOrganization())
                    .orElseThrow(() -> new EntityNotFoundException("Organización no encontrada."));

            event.setOrganization(organization);
        }
    }*/

    /**
     * Updates participants for an event
     * @param eventDto
     * @param event
     */
    /**
    public void updateParticipants(EventDto eventDto, Event event) {
        if (eventDto.getParticipants() != null && !eventDto.getParticipants().isEmpty()) {
            List<User> participants = new ArrayList<>();

            for (UserEventDto userDto : eventDto.getParticipants()) {
                if (userDto.getId() != null) {
                    User participant = userService.findById(userDto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Participante no encontrado con id: " + userDto.getId()));
                    participants.add(participant);
                }
            }

            event.setParticipants(participants);
        }
    }
    */
    /**
     * Finds an event by ID (returns Optional)
     * @param idEvent
     * @return Optional<Event>
     */
    @Override
    public Optional<Event> findById(Long idEvent) {
        return eventRepository.findById(idEvent);
    }

    /**
     * Gets all events with pagination and optional name filter
     * @param page
     * @param size
     * @param name
     * @return Page of events
     */
    @Override
    public Page<Event> getAllEvents(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")));

        if (name != null && !name.isEmpty()) {
            return eventRepository.findByNameContainingIgnoreCase(name, pageable);
        }

        return eventRepository.findAll(pageable);
    }

    /**
     * Finds an event by name
     * @param name
     * @return Optional<Event>
     */
    public Optional<Event> findEventByName(String name) {
        return eventRepository.findEventByName(name);
    }

    /**
     * Finds events by organizer
     * @param organizerId
     * @return List of events
     */
    public List<Event> getEventsByOrganizer(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    /**
     * Finds events that are not finished
     * @return List of active events
     */
    public List<Event> getActiveEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> !event.isFinished())
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .toList();
    }

    /**
     * Marks an event as finished
     * @param idEvent
     */
    public void markEventAsFinished(Long idEvent) {
        Optional<Event> eventOptional = eventRepository.findById(idEvent);

        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            event.setFinished(true);
            eventRepository.save(event);
        } else {
            throw new EntityNotFoundException("Evento con id " + idEvent + " no encontrado");
        }
    }

    /**
     * Creates a new event from the DTO with all relationships
     * @param eventDto
     * @return Event
     */
    public Event createEventFromDto(EventDto eventDto) {
        Event event = new Event();

        // Set basic fields
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setDirection(eventDto.getDirection());
        event.setDate(eventDto.getDate());

        if (eventDto.getLatitude() != null && eventDto.getLongitude() != null) {
            Point point = geometryFactory.createPoint(
                    new Coordinate(eventDto.getLongitude(), eventDto.getLatitude())
            );
            point.setSRID(4326);
            event.setLocation(point);
        }

        event.setFinished(eventDto.isFinished());

        // Assign organizer if provided
        if (eventDto.getIdOrganizer() != null) {
            User organizer = userService.findById(eventDto.getIdOrganizer())
                    .orElseThrow(() -> new EntityNotFoundException("Organizador no encontrado con id: " + eventDto.getIdOrganizer()));
            event.setOrganizer(organizer);
        }

        // Assign organization if provided
        if (eventDto.getIdOrganization() != null) {
            Organization organization = organizationService.findEntityById(eventDto.getIdOrganization())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Organización no encontrada con id: " + eventDto.getIdOrganization()
                    ));

            event.setOrganization(organization);
        }

        // Assign participants if provided

        if (eventDto.getParticipants() != null && !eventDto.getParticipants().isEmpty()) {
            List<User> participants = new ArrayList<>();

            for (UserEventDto userDto : eventDto.getParticipants()) {
                if (userDto.getId() != null) {
                    User participant = userService.findById(userDto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Participante no encontrado con id: " + userDto.getId()));
                    participants.add(participant);
                }
            }

            event.setParticipants(participants);
        }

        return eventRepository.save(event);
    }

    /**
     * Creates an event with organization and participant IDs (alternative method)
     * @param eventDto
     * @param organizationId
     * @param participantIds
     * @return Event
     */
    public Event createEventWithRelations(EventDto eventDto, Long organizationId, List<Long> participantIds) {
        Event event = new Event();

        // Set basic fields
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setDirection(eventDto.getDirection());
        event.setDate(eventDto.getDate());
        /**
        if (eventDto.getLatitude() != null && eventDto.getLongitude() != null) {
            Point point = geometryFactory.createPoint(
                    new Coordinate(eventDto.getLongitude(), eventDto.getLatitude())
            );
            point.setSRID(4326);
            event.setLocation(point);
        }        event.setFinished(eventDto.isFinished());
        **/
        // Assign organizer if provided
        if (eventDto.getIdOrganizer() != null) {
            User organizer = userService.findById(eventDto.getIdOrganizer())
                    .orElseThrow(() -> new EntityNotFoundException("Organizador no encontrado con id: " + eventDto.getIdOrganizer()));
            event.setOrganizer(organizer);
        }

        // Assign organization if provided
        /*if (organizationId != null) {
            Organization organization = organizationService.findById(organizationId)
                    .orElseThrow(() -> new EntityNotFoundException("Organización no encontrada con id: " + organizationId));
            event.setOrganization(organization);
        }*/

        // Assign participants if provided
        /**
        if (participantIds != null && !participantIds.isEmpty()) {
            List<User> participants = new ArrayList<>();

            for (Long participantId : participantIds) {
                User participant = userService.findById(participantId)
                        .orElseThrow(() -> new EntityNotFoundException("Participante no encontrado con id: " + participantId));
                participants.add(participant);
            }

            event.setParticipants(participants);
        }**/

        return eventRepository.save(event);
    }

    /**
     * Converts an Event to EventDto with all relationships
     * @param event
     * @return EventDto
     */
    public EventDto convertToDto(Event event) {
        EventDto dto = new EventDto();

        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setDirection(event.getDirection());
        dto.setDate(event.getDate());
       /**
        if (event.getLocation() != null) {
            dto.setLatitude(event.getLocation().getY()); // Latitud
            dto.setLongitude(event.getLocation().getX()); // Longitud
        }        dto.setFinished(event.isFinished());
        **/
        // Set organizer if exists
        if (event.getOrganizer() != null) {
            dto.setIdOrganizer(event.getOrganizer().getId());
        }

        // Set organization if exists
        if (event.getOrganization() != null) {
            dto.setIdOrganization(event.getOrganization().getId());
        }

        // Convert participants to UserDto list
        /**
        if (event.getParticipants() != null && !event.getParticipants().isEmpty()) {
            List<UserEventDto> participantDtos = event.getParticipants().stream()
                    .map(this::convertUserToDto)
                    .collect(Collectors.toList());
            dto.setParticipants(participantDtos);
        }**/

        return dto;
    }

    /**
     * Helper method to convert User to UserDto
     * @param user
     * @return UserDto
     */
    private UserEventDto convertUserToDto(User user) {
        UserEventDto userDto = new UserEventDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        // Add other fields as needed based on your UserDto structure
        return userDto;
    }

    // ================== PARTICIPANT MANAGEMENT ==================

    /**
     * Add a participant to an event
     * @param eventId
     * @param userId
     */

    /**
    public void addParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + eventId));

        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + userId));

        if (event.getParticipants() == null) {
            event.setParticipants(new ArrayList<>());
        }

        // Check if user is already a participant
        if (!event.getParticipants().contains(user)) {
            event.getParticipants().add(user);
            eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("El usuario ya es participante del evento");
        }
    }**/

    /**
     * Remove a participant from an event
     * @param eventId
     * @param userId
     */

    /**
    public void removeParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + eventId));

        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + userId));

        if (event.getParticipants() != null && event.getParticipants().contains(user)) {
            event.getParticipants().remove(user);
            eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("El usuario no es participante del evento");
        }
    }**/

    /**
     * Get all participants of an event
     * @param eventId
     * @return List of participants
     */
    /**
    public List<User> getEventParticipants(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + eventId));

        return event.getParticipants() != null ? event.getParticipants() : new ArrayList<>();
    }**/

    /**
     * Get events where a user is a participant
     * @param userId
     * @return List of events
     */

    /**
    public List<Event> getEventsByParticipant(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + userId));

        return eventRepository.findAll().stream()
                .filter(event -> event.getParticipants() != null && event.getParticipants().contains(user))
                .collect(Collectors.toList());
    }**/

    /**
     * Check if a user is a participant in an event
     * @param eventId
     * @param userId
     * @return boolean
     */
    /**
    public boolean isUserParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + eventId));

        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + userId));

        return event.getParticipants() != null && event.getParticipants().contains(user);
    }**/

    /**
     * Get participant count for an event
     * @param eventId
     * @return int
     */

    /**
    public int getParticipantCount(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con id: " + eventId));

        return event.getParticipants() != null ? event.getParticipants().size() : 0;
    }**/


}