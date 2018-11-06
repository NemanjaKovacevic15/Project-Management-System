package com.get.repository;

import com.get.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findUserByUsername(String username);

    Integer deleteByUsername(String username);

    List<User> findUsersByUserRoles_Name(String username);

    int countAllByIdIsNotNull();

    List<User> findByUsernameContainingOrFirstNameContainingOrLastNameContaining(String q1, String q2, String q3, Pageable pageable);
}
