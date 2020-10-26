package com.wolox.service;

import com.wolox.models.User;
import com.wolox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public   Iterable<User> getUsers() {
        return repository.findAll();
    }
}
