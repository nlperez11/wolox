package com.wolox.controller;

import com.wolox.service.AlbumService;
import com.wolox.service.PhotoService;
import com.wolox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;

    @GetMapping(path = "/user")
    public ResponseEntity getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping(path = "/album")
    public ResponseEntity getAlbums() {
        return ResponseEntity.ok(albumService.getAlbums());
    }

    @GetMapping(path = "/photo")
    public ResponseEntity getPhotos() {
        return ResponseEntity.ok(photoService.getPhotos());
    }

    @GetMapping(path = "/photo/user/{id}")
    public ResponseEntity getPhotosByUser(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(photoService.getPhotosByUser(id));
    }
}
