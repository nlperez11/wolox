package com.wolox.service;

import com.wolox.models.Album;
import com.wolox.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository repository;

    public Iterable<Album> getAlbums() {
        return repository.findAll();
    }
}
