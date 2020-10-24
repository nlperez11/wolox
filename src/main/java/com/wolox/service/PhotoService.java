package com.wolox.service;

import com.wolox.models.Photo;
import com.wolox.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository repository;

    public Iterable<Photo> getPhotos() {
        return repository.findAll();
    }

}
