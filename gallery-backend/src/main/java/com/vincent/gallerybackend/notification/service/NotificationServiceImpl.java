package com.vincent.gallerybackend.notification.service;

import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.notification.model.Notification;
import com.vincent.gallerybackend.notification.model.NotificationEntity;
import com.vincent.gallerybackend.notification.model.NotificationMapper;
import com.vincent.gallerybackend.notification.repository.NotificationRepository;
import com.vincent.gallerybackend.user.model.UserEntity;
import com.vincent.gallerybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    @Override
    public List<Notification> getNotificationsFromUserByUserEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<NotificationEntity> notificationEntities = notificationRepository.findAllById(userEntity.getNotificationIds());
        return notificationMapper.notificationsDAOtoDTO(notificationEntities);
    }

    @Transactional()
    @Override
    public Notification createNotification(Notification notification) {
        NotificationEntity notificationEntity = notificationMapper.notificationDTOtoDAO(notification);
        notificationEntity.setDateTime(LocalDateTime.now());
        sendNewNotificationToAllUsers(notification);
        notificationRepository.save(notificationEntity);
        return notificationMapper.notificationDAOtoDTO(notificationEntity);
    }

    private void sendNewNotificationToAllUsers(Notification notification) {
        List<UserEntity> users =  userRepository.findAll();
        for (UserEntity user : users) {
            Set<Long> notificationIds = user.getNotificationIds();
            notificationIds.add(notification.getId());
            user.setNotificationIds(notificationIds);
            userRepository.save(user);
        }
    }

    @Transactional()
    @Override
    public Notification setNotificationAsRead(long id) {
        NotificationEntity notificationEntity = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        notificationEntity.setRead(true);
        notificationRepository.save(notificationEntity);
        return notificationMapper.notificationDAOtoDTO(notificationEntity);
    }

}
