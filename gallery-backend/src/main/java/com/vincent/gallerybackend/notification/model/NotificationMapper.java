    package com.vincent.gallerybackend.notification.model;

    import org.mapstruct.Mapper;

    import java.util.List;

    @Mapper(componentModel = "spring")
    public interface NotificationMapper {

        Notification notificationDAOtoDTO(NotificationEntity notificationEntity);

        NotificationEntity notificationDTOtoDAO(Notification notification);

        List<Notification> notificationsDAOtoDTO(List<NotificationEntity> notificationEntities);

    }
