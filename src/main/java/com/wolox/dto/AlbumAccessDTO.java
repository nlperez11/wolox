package com.wolox.dto;

import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.models.User;
import lombok.Data;

@Data
public class AlbumAccessDTO {

    private int id;
    private Album album;
    private User user;
    private boolean write;
    private boolean read;

    public AlbumAccessDTO(AlbumAccess albumAccess) {
        this.id = albumAccess.getId();
        this.album = new Album().setId(albumAccess.getAlbum().getId()).setTitle(albumAccess.getAlbum().getTitle());
        this.user = new User()
                .setId(albumAccess.getUser().getId())
                .setName(albumAccess.getUser().getName())
                .setUsername(albumAccess.getUser().getUsername())
                .setEmail(albumAccess.getUser().getEmail())
                .setPhone(albumAccess.getUser().getPhone())
                .setWebsite(albumAccess.getUser().getWebsite());
    }
}
