package com.get.service;

import com.get.model.UserRole;
import com.get.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

//This service class contains business logic for user-role part and manipulate's with Database;
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public UserRole findByRoleName(String type) {
        return userRoleRepository.findOne(type);
    }

    @Override
    public List<UserRole> findAll() {
        return (List<UserRole>) userRoleRepository.findAll();
    }

    @Override
    public List<UserRole> findRoleByUserId(Long userId) {
        return null;
    }
}
