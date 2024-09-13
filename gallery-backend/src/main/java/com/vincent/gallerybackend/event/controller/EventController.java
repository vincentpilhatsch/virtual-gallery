package com.vincent.gallerybackend.event.controller;

import com.vincent.gallerybackend.event.model.Event;
import com.vincent.gallerybackend.event.model.EventEntity;
import com.vincent.gallerybackend.event.service.EventServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/event")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceImpl eventServiceImpl;

    @GetMapping(value = "/all", produces = "application/json")
    public List<Event> getEvents() {
        List<Event> events = eventServiceImpl.getEvents();
        log.info("GET ALL events ({}): {}", events.size(), events);
        return events;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Event getEventById(@PathVariable long id) {
        Event event = eventServiceImpl.getEventById(id);
        log.info("GET event: {}", event);
        return event;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<EventEntity> createEvent(@Valid @RequestPart("event") Event event, // RequestParam?
                                                   @RequestPart("file") MultipartFile imageFile) {
        EventEntity eventEntity = eventServiceImpl.createEvent(event, imageFile);
        log.info("CREATE event: {}", event);
        return new ResponseEntity<>(eventEntity, HttpStatus.CREATED);
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<EventEntity> updateEvent(@Valid @RequestPart("event") Event newEvent,
                                                   @RequestPart("file") MultipartFile imageFile) {
        EventEntity eventEntity = eventServiceImpl.updateEvent(newEvent, imageFile);
        log.info("UPDATE event: {}", newEvent);
        return new ResponseEntity<>(eventEntity, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        eventServiceImpl.deleteEvent(id);
        log.info("DELETE event with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
