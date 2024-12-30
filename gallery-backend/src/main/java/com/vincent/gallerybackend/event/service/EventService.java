package com.vincent.gallerybackend.event.service;

import com.vincent.gallerybackend.event.model.Event;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface EventService {

    List<Event> getEvents();

    Event getEventById(long id);

    Event createEvent(Event event, MultipartFile multipartFile);

    Event updateEvent(Event newEvent);

    Event updateEvent(Event event, MultipartFile multipartFile);

    void deleteEvent(long id);

}
