package com.wolox.controller;

import com.wolox.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "api/comment", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CommentController {

    @Autowired
    private CommentService service;

    @GetMapping
    public ResponseEntity getCommentsByName(@RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "email", required = false) String email) throws IOException, InterruptedException {
        return ResponseEntity.ok(service.getComments(name, email));
    }
}
