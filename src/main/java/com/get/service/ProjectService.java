package com.get.service;

import com.get.model.Project;

import java.util.List;

public interface ProjectService {

    public void save(Project project);

    public List<Project> findAll();

    public Project findOne(Long id);

    public void updateProject(Project project);

    public List<Project> findByName(String name);

}
