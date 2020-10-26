package com.wolox.service;

import com.wolox.controller.Exception.AlbumAccessException;
import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.repository.AlbumAccessRepository;
import com.wolox.repository.AlbumRepository;
import com.wolox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumAccessRepository albumAccessRepository;

    public Iterable<Album> getAlbums() {
        return repository.findAll();
    }

    public AlbumAccess createSharedAlbum(AlbumAccess albumAccess) throws AlbumAccessException {

        this.checkUser(albumAccess);
        this.checkAlbum(albumAccess);

        return albumAccessRepository.save(albumAccess);
    }

    private AlbumAccess checkUser(AlbumAccess albumAccess) throws AlbumAccessException {
        return userRepository.findById(albumAccess.getUser().getId())
                .map(albumAccess::setUser)
                .orElseThrow(() -> new AlbumAccessException("User not found, " + albumAccess.getUser().getId()));
    }
    private AlbumAccess checkAlbum(AlbumAccess albumAccess) throws AlbumAccessException {
        return repository.findById(albumAccess.getAlbum().getId())
                .map(albumAccess::setAlbum)
                .orElseThrow(() -> new AlbumAccessException("Album not found, " + albumAccess.getAlbum().getId()));
    }
}
