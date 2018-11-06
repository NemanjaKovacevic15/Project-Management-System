package com.get.service;

import java.util.List;

import com.get.repository.UserRepository;
import com.get.util.SecurityUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.get.model.User;

//This service class contains business logic for User and manipulate's with Database; 
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SecurityUtils securityUtils;

    @Autowired
    private AuthenticationTrustResolver authenticationTrustResolver;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            Hibernate.initialize(user.getUserRoles());
        }
        return user;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(securityUtils.encodePassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        user.setPassword(securityUtils.encodePassword(user.getPassword()));
        return userRepository.save(user);

    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public List<User> findAllUsers(Pageable pageable) {

        List<User> users = userRepository.findAll(pageable).getContent();

        Hibernate.initialize(users);

        return users;
    }

    @Override
    public boolean isUserUsernameUnique(Long id, String username) {
        User user = findByUsername(username);
        return (user == null || ((id != null) && (user.getId() == id)));
    }

    @Override
    public List<User> findUsersByRoleName(String roleName) {
        return userRepository.findUsersByUserRoles_Name(roleName);
    }

    @Override
    public User getCurrentAuthenticatedUser() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        return findByUsername(username);
    }

    @Override
    public boolean isUserAuthenticated() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authenticationTrustResolver.isAnonymous(authentication);

    }

    @Override
    public int totalRecord() {
        return userRepository.countAllByIdIsNotNull();
    }

    @Override
    public List<User> filterUserByCriteria(String query, Pageable pageable) {
        return userRepository
                .findByUsernameContainingOrFirstNameContainingOrLastNameContaining(query, query, query, pageable);
    }

}
