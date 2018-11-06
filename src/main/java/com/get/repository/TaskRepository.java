package com.get.repository;

import com.get.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

//Extension of CrudRepository to provide additional methods to retrieve entities using
//the pagination and sorting abstraction.
@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {

    List<Task> findByDeveloper_Id(Long userId);

    int countAllByIdIsNotNull();

    List<Task> findByTitleContaining(
            String q1, Pageable pageable);

}
