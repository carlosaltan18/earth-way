package com.uvg.earth.way.service;


import com.uvg.earth.way.dto.EventDto;
import com.uvg.earth.way.exception.EventDeletionException;
import com.uvg.earth.way.model.Event;
import com.uvg.earth.way.repository.EventRepository;
import com.uvg.earth.way.repository.UserRepository;
import com.uvg.earth.way.service.interfaces.IEventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService  implements IEventService {

    private EventRepository eventRepository;
    private final UserService userService;

    @Override
    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public void saveEvent(Event event){
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long idEvent) {
        if(!eventRepository.existsById(idEvent)){
            throw new IllegalArgumentException("No se en" +
                    "contro el evento con el id: " + idEvent);
        }
        try{
            eventRepository.deleteById(idEvent);
        }catch(DataAccessException e){
            throw new EventDeletionException("Error eliminando el evento con id" + idEvent, e);
        }
    }

    @Override
    public void updateEvent(EventDto eventDto, Long idEvent) {
        validateEventDto(idEvent);
        Optional<Event> eventOptional = eventRepository.findById(idEvent);
        if(eventOptional.isPresent()){
            Event event = eventOptional.get();
            if(eventDto.getName() != null) event.setName(eventDto.getName());
            if(eventDto.getDescription() != null) event.setDescription(eventDto.getDescription());
            if(eventDto.getDirection()!= null) event.setDirection(eventDto.getDirection());
            if(eventDto.getDate()!= null) event.setDate(eventDto.getDate());
            if(eventDto.getLocation() != null) event.setLocation(eventDto.getLocation());
            if(eventDto.getDate() != null) event.setDate(eventDto.getDate());
            if(eventDto.getIdOrganizer() != null && eventDto.getIdOrganizer() != null){
                //User user = userService
            };



        }
    }

    public void validateEventDto(Long idEvent){
        if(!eventRepository.existsById(idEvent)){
            throw new EntityNotFoundException("Evento" + idEvent + "no existe");
        }
    }


    @Override
    public Optional<Event> findById(Long idEvent) {
        return eventRepository.findById(idEvent);
    }

    @Override
    public Page<Event> getAllEvents(int page, int size, String name) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")));

        if(name != null && !name.isEmpty()){
            return eventRepository.findByNameContainingIgnoreCase(name, pageable);
        }

        Page<Event> events = eventRepository.findAll(pageable);

        return events;
    }


}
