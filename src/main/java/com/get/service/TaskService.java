package com.get.service;

import com.get.model.Project;
import com.get.model.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    List<Task> findAll(Pageable pageable);

    Task save(Task workOrder);

    public void deleteById(Long id);

    Task findOne(Long id);

    List<Task> getMyTasks();

    int totalRecord();

    List<Task> filterTaskByCriteria(String query, Pageable pageable);
    

}
