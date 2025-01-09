package com.vincent.gallerybackend.event.service;

import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.config.FileValidationService;
import com.vincent.gallerybackend.event.model.Event;
import com.vincent.gallerybackend.event.model.EventEntity;
import com.vincent.gallerybackend.event.model.EventMapper;
import com.vincent.gallerybackend.event.repository.EventRepository;
import com.vincent.gallerybackend.exceptions.ImageFileException;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final FileValidationService fileValidationService;

    @Transactional(readOnly = true)
    @Override
    public List<Event> getEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        return eventMapper.eventsDAOtoDTO(eventEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public Event getEventById(long id) {
        EventEntity eventEntity = findById(id);
        return eventMapper.eventDAOtoDTO(eventEntity);
    }

    private EventEntity findById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
    }

    @Transactional()
    @Override
    public Event createEvent(Event event, MultipartFile imageFile) {
        EventEntity eventEntity = eventMapper.eventDTOtoDAO(event);
        setEventImage(eventEntity, imageFile);
        eventRepository.save(eventEntity);
        return eventMapper.eventDAOtoDTO(eventEntity);
    }

    @Transactional()
    @Override
    public Event updateEvent(Event newEvent) {
        EventEntity oldEvent = findById(newEvent.getId());
        EventEntity updatedEventEntity = eventMapper.updateEventDTOtoDAO(oldEvent, newEvent);
        eventRepository.save(updatedEventEntity);
        return eventMapper.eventDAOtoDTO(updatedEventEntity);
    }


    @Transactional()
    @Override
    public Event updateEvent(Event newEvent, MultipartFile imageFile) {
        EventEntity oldEvent = findById(newEvent.getId());
        EventEntity updatedEventEntity = eventMapper.updateEventDTOtoDAO(oldEvent, newEvent);
        setEventImage(updatedEventEntity, imageFile);
        eventRepository.save(updatedEventEntity);
        return eventMapper.eventDAOtoDTO(updatedEventEntity);
    }

    private void setEventImage(EventEntity eventEntity, MultipartFile imageFile) {
        fileValidationService.validateFile(imageFile);
        fileValidationService.safeImageInEntity(eventEntity, imageFile);
    }


    @Transactional()
    @Override
    public void deleteEvent(long id) {
        EventEntity eventEntity = findById(id);
        eventRepository.delete(eventEntity);
    }

}
