package com.wolox.controller;

import com.wolox.controller.Exception.AlbumAccessException;
import com.wolox.controller.validator.AlbumAccessValidator;
import com.wolox.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/album/access", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AlbumController {

    @Autowired
    private AlbumService service;

    @PostMapping
    public ResponseEntity createSharedAlbum(@Valid @RequestBody AlbumAccessValidator body) throws AlbumAccessException {
        return ResponseEntity.ok(service.createSharedAlbum(body.get()));
    }

    @PutMapping
    public ResponseEntity updateSharedAlbum(@Valid @RequestBody AlbumAccessValidator body) throws AlbumAccessException {
        return ResponseEntity.ok(service.updateSharedAlbum(body.get()));
    }
}
