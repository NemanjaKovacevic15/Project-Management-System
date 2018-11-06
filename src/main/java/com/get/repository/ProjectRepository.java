package com.get.repository;

import com.get.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
//CRUD Repository interface provides us some basic crud methods and gives us possibility to add and modify 
//our implementation for manipulating DB.
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @Query("select p from Project p where p.name LIKE  %?1% ")
    public List<Project> findByName(@Param("name") String name);

}
