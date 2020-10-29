package com.wolox.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wolox.models.AlbumAccess;
import com.wolox.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AlbumAccessDTO {

    private int id;
    private User user;
    private boolean write;
    private boolean read;

    public AlbumAccessDTO(AlbumAccess albumAccess) {
        this.id = albumAccess.getId();
        this.user = albumAccess.getUser();
        this.read = albumAccess.isRead();
        this.write = albumAccess.isWrite();
    }
}
