package com.example.notificationservice.API.Hibernate.DAO;

import com.example.notificationservice.API.Models.NotificationModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class NotificationDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public NotificationDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveNotification(NotificationModel notificationModel) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(notificationModel);
    }
}
