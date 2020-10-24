package com.wolox.controller;

import com.wolox.service.AlbumService;
import com.wolox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private AlbumService albumService;

    @GetMapping(path = "/user")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping(path = "/album")
    public ResponseEntity getAlbums() {
        return ResponseEntity.ok(albumService.getAlbums());
    }
}