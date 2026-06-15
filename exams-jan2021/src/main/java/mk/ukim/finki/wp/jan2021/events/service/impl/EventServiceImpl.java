package mk.ukim.finki.wp.jan2021.events.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.jan2021.events.model.Event;
import mk.ukim.finki.wp.jan2021.events.model.EventType;
import mk.ukim.finki.wp.jan2021.events.model.exceptions.InvalidEventIdException;
import mk.ukim.finki.wp.jan2021.events.repository.EventRepository;
import mk.ukim.finki.wp.jan2021.events.service.EventLocationService;
import mk.ukim.finki.wp.jan2021.events.service.EventService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventLocationService eventLocationService;

    @Override
    public List<Event> listAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(InvalidEventIdException::new);
    }

    @Override
    public Event create(String name, String description, Double price, EventType type, Long location) {
        Event event = new Event(name, description, price, type, eventLocationService.findById(location));

        return eventRepository.save(event);
    }

    @Override
    public Event update(Long id, String name, String description, Double price, EventType type, Long location) {
        Event event = findById(id);

        event.setName(name);
        event.setDescription(description);
        event.setPrice(price);
        event.setType(type);
        event.setLocation(eventLocationService.findById(location));

        return eventRepository.save(event);
    }

    @Override
    public Event delete(Long id) {
        Event event = findById(id);
        eventRepository.delete(event);

        return event;
    }

    @Override
    public Event like(Long id) {
        Event event = findById(id);
        event.setLikes(event.getLikes() + 1);

        return eventRepository.save(event);
    }

    @Override
    public List<Event> listEventsWithPriceLessThanAndType(Double price, EventType type) {
        if(price != null && type != null) {
            return eventRepository.findAll()
                    .stream()
                    .filter(event -> event.getPrice() <= price && event.getType() == type)
                    .toList();
        }

        if(price != null) {
            return eventRepository.findAll()
                    .stream()
                    .filter(event -> event.getPrice() <= price)
                    .toList();
        }

        if(type != null){
            return eventRepository.findAll()
                    .stream()
                    .filter(event -> event.getType() == type)
                    .toList();
        }

        return eventRepository.findAll();
    }
}
