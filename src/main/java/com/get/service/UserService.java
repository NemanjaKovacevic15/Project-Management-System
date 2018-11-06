package com.get.service;

import java.util.List;

import com.get.model.User;
import org.springframework.data.domain.Pageable;

public interface UserService {
    
    User findById(Long id);

    User findByUsername(String username);

    User saveUser(User user);

    User updateUser(User user);

    void deleteUserByUsername(String username);

    List<User> findAllUsers(Pageable pageable);

    boolean isUserUsernameUnique(Long id, String username);

    /**
     * Finds user by having a role of @roleName
     *
     * @param roleName
     * @return @{@link List<User>}
     */
    List<User> findUsersByRoleName(String roleName);

    /**
     * Returns currently authenticated user
     *
     * @return @{@link User}
     */
    User getCurrentAuthenticatedUser();

    /**
     * This method returns true if users is already authenticated [logged-in],
     * else false.
     *
     * @return boolean
     */
    boolean isUserAuthenticated();

    int totalRecord();

    List<User> filterUserByCriteria(String query, Pageable pageable);

}
