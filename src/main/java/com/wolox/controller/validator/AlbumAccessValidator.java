package com.wolox.controller.validator;

import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.models.User;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AlbumAccessValidator {

    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer albumId;

    private boolean write;

    private boolean read;

    public AlbumAccess get() {
        return new AlbumAccess()
                .setId(this.id)
                .setUser(new User().setId(this.userId))
                .setAlbum(new Album().setId(this.albumId))
                .setRead(this.read)
                .setWrite(this.write);
    }
}
