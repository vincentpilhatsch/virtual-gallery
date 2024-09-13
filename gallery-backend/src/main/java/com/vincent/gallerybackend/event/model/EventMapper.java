    package com.vincent.gallerybackend.event.model;

    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;

    import java.util.List;

    @Mapper(componentModel = "spring")
    public interface EventMapper {

        Event eventDAOtoDTO(EventEntity eventEntity);

        EventEntity eventDTOtoDAO(Event event);

        List<Event> eventsDAOtoDTO(List<EventEntity> eventEntities);

        @Mapping(target = "id", source = "oldEventEntity.id")
        @Mapping(target = "title", source = "newEvent.title")
        @Mapping(target = "description", source = "newEvent.description")
        @Mapping(target = "dateTime", source = "newEvent.dateTime")
        @Mapping(target = "image", source = "newEvent.image")
        @Mapping(target = "confirmedUserIds", source = "newEvent.confirmedUserIds")
        EventEntity updateEventDTOtoDAO(EventEntity oldEventEntity, Event newEvent);

    }
