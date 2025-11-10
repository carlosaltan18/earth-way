package com.uvg.earth.way.controller;

import com.uvg.earth.way.dto.EventDto;
import com.uvg.earth.way.model.Event;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.security.SecurityConfig;
import com.uvg.earth.way.service.EventService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RequestMapping(value = "/api/v1/event")
@RestController
public class EventController {

    private final EventService eventService;
    private final static String MESSAGE = "message";
    private final static String ERROR = "error";
    private final static String PAYLOAD = "payload";
    private final static String ADMIN = "ADMIN";
    private final static String USER = "USER";
    private final static String ORGANIZATION = "ORGANIZATION";

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(required= false) String email){
        Map<String, Object> response = new HashMap<>();

        try{
            Page<EventDto> eventPage = eventService.getAllEvents(page, size, email);
            response.put("payload", eventPage.getContent());
            response.put(MESSAGE, "Events retrieved successfully");

            response.put("events", eventPage.getContent());
            response.put("totalEvents", eventPage.getTotalPages());
            response.put("currentPage", eventPage.getNumber());
            response.put("totalElements", eventPage.getTotalElements());

            return ResponseEntity.ok(response);

        }catch(Exception e){
            response.put(MESSAGE, ERROR);
            response.put("err", "There is an error getting events" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("/{idEvent}")
    public ResponseEntity<Map<String, Object>> findEvent(@PathVariable Long idEvent) {
        Map<String, Object> response = new HashMap<>();
        try{
            Optional<Event> event = eventService.findById(idEvent);
            if(event.isPresent()){
                // CORREGIDO: Cuando existe, devolver OK con el evento
                response.put(PAYLOAD, eventService.convertToDto(event.get()));
                response.put(MESSAGE, "Event found successfully");
                return ResponseEntity.ok(response);
            }else {
                // CORREGIDO: Cuando no existe, devolver not found
                response.put(MESSAGE, "Event not found");
                return ResponseEntity.notFound().build();
            }
        }catch(IllegalArgumentException e){
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch(Exception e){
            response.put(MESSAGE, ERROR);
            response.put("err", "There is an error getting event: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @RolesAllowed({ADMIN})
    @PostMapping(value="/create")
    public ResponseEntity<Map<String,Object>> createEvent(@RequestBody EventDto eventDto){
        Map<String, Object> response = new HashMap<>();
        try{
            Event createEvent = eventService.createEventFromDto(eventDto);
            response.put(PAYLOAD, eventService.convertToDto(createEvent));
            response.put(MESSAGE, "Event has been created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }catch(IllegalArgumentException e){
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);

        }catch(Exception e){
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error creating event" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @PutMapping("/{idEvent}")
    public ResponseEntity<Map<String, Object>> updateEvent(@PathVariable Long idEvent, @RequestBody EventDto eventDto){
        Map<String, Object> response = new HashMap<>();
        try{
            eventService.updateEvent(eventDto, idEvent);
            Optional<Event> updatedEvent = eventService.findById(idEvent);
            response.put(PAYLOAD, eventService.convertToDto(updatedEvent.get()));
            response.put(MESSAGE, "Event has been updated successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(IllegalArgumentException e){
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch(Exception e){
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error updating event" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({ADMIN})
    @DeleteMapping("/{idEvent}")
    public ResponseEntity<Map<String, Object>> deleteEvent(@PathVariable Long idEvent) {
        Map<String, Object> response = new HashMap<>();

        try {
            eventService.deleteEvent(idEvent);
            response.put(MESSAGE, "Event deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error deleting event: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    //          SPECIFIC RESEARCH METHODS


    @RolesAllowed({USER})
    @PostMapping("/{eventId}/participants")
    public ResponseEntity<Map<String, Object>> addParticipant(
            @PathVariable Long eventId) {

        Map<String, Object> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customUserDetails = (User) authentication.getPrincipal();
        try {
            eventService.addParticipant(eventId, customUserDetails.getId());
            response.put(MESSAGE, "Participant added successfully");
            response.put("participantCount", eventService.getParticipantCount(eventId));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error adding participant: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }



    @RolesAllowed({USER})
    @DeleteMapping("/{eventId}/participants")
    public ResponseEntity<Map<String, Object>> removeParticipant(
            @PathVariable Long eventId) {

        Map<String, Object> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customUserDetails = (User) authentication.getPrincipal();
        try {
            eventService.removeParticipant(eventId, customUserDetails.getId());
            response.put(MESSAGE, "Participant removed successfully");
            response.put("participantCount", eventService.getParticipantCount(eventId));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error removing participant: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }




    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("/{eventId}/participants")
    public ResponseEntity<Map<String, Object>> getEventParticipants(@PathVariable Long eventId) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<User> participants = eventService.getEventParticipants(eventId);
            response.put(PAYLOAD, participants);
            response.put(MESSAGE, "Event participants retrieved successfully");
            response.put("totalParticipants", participants.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error getting event participants: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


/**
    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("/{eventId}/participants/{userId}/check")
    public ResponseEntity<Map<String, Object>> isUserParticipant(
            @PathVariable Long eventId,
            @PathVariable Long userId) {

        Map<String, Object> response = new HashMap<>();

        try {
            boolean isParticipant = eventService.isUserParticipant(eventId, userId);
            response.put(PAYLOAD, isParticipant);
            response.put(MESSAGE, "Participation status checked successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error checking participation status: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    **/


    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @GetMapping("/{eventId}/participants/count")
    public ResponseEntity<Map<String, Object>> getParticipantCount(@PathVariable Long eventId) {
        Map<String, Object> response = new HashMap<>();

        try {
            int count = eventService.getParticipantCount(eventId);
            response.put(PAYLOAD, count);
            response.put(MESSAGE, "Participant count retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("error", "There is an error getting participant count: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }








}
