package com.get.service;

import com.get.exception.TaskNotFoundException;
import com.get.model.Task;
import com.get.repository.TaskRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//This service class contains business logic for task and manipulate's with Database; 
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable).getContent();
    }

    @Override
    public Task save(Task workOrder) {
        return taskRepository.save(workOrder);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.delete(id);
    }

    @Override
    public Task findOne(Long id) {
        Task workOrder = taskRepository.findOne(id);
        if (workOrder == null) {
            throw new TaskNotFoundException("Unable to find task with task Id: " + id);
        }
        Hibernate.initialize(workOrder.getDeveloper());
        return workOrder;
    }

    @Override
    public List<Task> getMyTasks() {
        return taskRepository.findByDeveloper_Id(userService.getCurrentAuthenticatedUser().getId());
    }

    @Override
    public int totalRecord() {
        return taskRepository.countAllByIdIsNotNull();
    }

    @Override
    public List<Task> filterTaskByCriteria(String query, Pageable pageable) {
        return taskRepository
                .findByTitleContaining(query, pageable);
    }

}
