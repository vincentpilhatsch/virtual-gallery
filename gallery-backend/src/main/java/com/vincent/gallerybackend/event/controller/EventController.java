package com.vincent.gallerybackend.event.controller;

import com.vincent.gallerybackend.event.model.Event;
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
    public ResponseEntity<List<Event>> getEvents() {
        List<Event> events = eventServiceImpl.getEvents();
        log.info("GET ALL events ({})", events.size());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Event> getEvent(@PathVariable long id) {
        Event event = eventServiceImpl.getEventById(id);
        log.info("GET event: {}", event.getTitle());
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<Event> createEvent(@Valid @RequestPart("event") Event event,
                                             @RequestPart("file") MultipartFile imageFile) {
        Event createdEntity = eventServiceImpl.createEvent(event, imageFile);
        log.info("CREATE event: {}", event.getTitle());
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<Event> updateEvent(@Valid @RequestPart("event") Event newEvent,
                                                   @RequestPart("file") MultipartFile imageFile) {
        Event updatedEvent = eventServiceImpl.updateEvent(newEvent, imageFile);
        log.info("UPDATE event: {}", newEvent.getTitle());
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        eventServiceImpl.deleteEvent(id);
        log.info("DELETE event with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
