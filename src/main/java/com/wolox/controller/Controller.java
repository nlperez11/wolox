package com.wolox.controller;

import com.wolox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity getUsers() throws IOException, InterruptedException {
        // service.synchronizeUsers();
        // service.synchronizeAlbums();
        service.synchronizePhotos();
        return ResponseEntity.ok().build();
    }
}
