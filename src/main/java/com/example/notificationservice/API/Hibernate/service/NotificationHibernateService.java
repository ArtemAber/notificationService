package com.example.notificationservice.API.Hibernate.service;

import com.example.notificationservice.API.Hibernate.DAO.NotificationDAO;
import com.example.notificationservice.API.Models.NotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class NotificationHibernateService {

    private final NotificationDAO notificationDAO;

    @Autowired
    public NotificationHibernateService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Transactional
    public void saveNotification(NotificationModel notificationModel) {
        notificationDAO.saveNotification(notificationModel);
    }
}
