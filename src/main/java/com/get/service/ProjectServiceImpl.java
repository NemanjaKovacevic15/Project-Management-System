package com.get.service;

import com.get.model.Project;
import com.get.repository.ProjectRepository;
import com.get.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

//This service class contains business logic for project and manipulate's with Database; 
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Override
    public void save(Project project) {
        projectRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return (List<Project>) projectRepository.findAll();
    }

    @Override
    public Project findOne(Long id) {
        return projectRepository.findOne(id);
    }

    @Override
    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public List<Project> findByName(String name) {
        return (List<Project>) projectRepository.findByName(name);
    }
}
