package com.wolox.service;

import com.wolox.models.User;
import com.wolox.repository.UserRpository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRpository repository;

    public   Iterable<User> getUsers() {
        return repository.findAll();
    }
}
