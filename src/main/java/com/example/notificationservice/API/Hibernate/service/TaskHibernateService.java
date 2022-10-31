package com.example.notificationservice.API.Hibernate.service;

import com.example.notificationservice.API.Hibernate.DAO.TaskDAO;
import com.example.notificationservice.API.ThreadController.Models.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TaskHibernateService {

    private final TaskDAO taskDAO;

    @Autowired
    public TaskHibernateService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Transactional
    public void saveTask(TaskModel taskModel) {
        taskDAO.saveTask(taskModel);
    }

    @Transactional
    public void deleteTask(TaskModel taskModel) {
        taskDAO.deleteTask(taskModel);
    }

    @Transactional
    public List<TaskModel> getAllTask() {
        return taskDAO.getAllTask();
    }
}
