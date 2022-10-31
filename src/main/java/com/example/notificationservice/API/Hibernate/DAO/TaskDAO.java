package com.example.notificationservice.API.Hibernate.DAO;

import com.example.notificationservice.API.ThreadController.Models.TaskModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TaskDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveTask(TaskModel task) {
        Session session = sessionFactory.getCurrentSession();
        session.save(task);
    }

    public void deleteTask(TaskModel task) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(task);
    }

    public List<TaskModel> getAllTask() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select a from TaskModel a", TaskModel.class).getResultList() != null ?
                session.createQuery("select a from TaskModel a", TaskModel.class).getResultList() : null;
    }
}
