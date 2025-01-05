package com.vincent.gallerybackend.event.controller;

import com.vincent.gallerybackend.event.model.Event;
import com.vincent.gallerybackend.event.service.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Retrieve all events", description = "Fetches a list of all events available in the system.")
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Event>> getEvents() {
        List<Event> events = eventServiceImpl.getEvents();
        log.info("GET ALL events ({})", events.size());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve event by ID", description = "Fetches details of a specific event using its unique ID.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Event> getEvent(@PathVariable long id) {
        Event event = eventServiceImpl.getEventById(id);
        log.info("GET event: {}", event.getTitle());
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @Operation(summary = "Create a new event", description = "Creates a new event with the provided details and an associated image.")
    @PostMapping(produces = "application/json")
    public ResponseEntity<Event> createEvent(@Valid @RequestPart("event") Event event,
                                             @RequestPart("file") MultipartFile imageFile) {
        Event createdEntity = eventServiceImpl.createEvent(event, imageFile);
        log.info("CREATE event: {}", event.getTitle());
        return new ResponseEntity<>(createdEntity, HttpStatus.CREATED);
    }

    @Operation(summary = "Update event with image", description = "Updates the details of an existing event and replaces its associated image.")
    @PutMapping(value = "update", produces = "application/json")
    public ResponseEntity<Event> updateEvent(@Valid @RequestPart("event") Event newEvent) {
        Event updatedEvent = eventServiceImpl.updateEvent(newEvent);
        log.info("UPDATE event: {}", newEvent.getTitle());
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @Operation(summary = "Update event with image", description = "Updates the details of an existing event and replaces its associated image.")
    @PutMapping(value = "updateWithImage", produces = "application/json")
    public ResponseEntity<Event> updateEvent(@Valid @RequestPart("event") Event newEvent,
                                             @RequestPart("file") MultipartFile imageFile) {
        Event updatedEvent = eventServiceImpl.updateEvent(newEvent, imageFile);
        log.info("UPDATE event with image: {}", newEvent.getTitle());
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @Operation(summary = "Delete event by ID", description = "Deletes an existing event using its unique ID.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) {
        eventServiceImpl.deleteEvent(id);
        log.info("DELETE event with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
