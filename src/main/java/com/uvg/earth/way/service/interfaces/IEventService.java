package com.uvg.earth.way.service.interfaces;

import com.uvg.earth.way.dto.EventDto;
import com.uvg.earth.way.model.Event;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IEventService {

    Event findEventById(Long id);

    void saveEvent(Event event);

    void deleteEvent(Long idEvent);

    void updateEvent(EventDto eventDto, Long idEvent);

    Optional<Event> findById(Long idEvent);

    Page<EventDto> getAllEvents(int page, int size, String name);

}
