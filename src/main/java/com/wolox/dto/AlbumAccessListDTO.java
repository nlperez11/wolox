package com.wolox.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wolox.models.Album;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlbumAccessListDTO {

    private Album album;
    private List<AlbumAccessDTO> users;

    public AlbumAccessListDTO(Album album, List<AlbumAccessDTO> list) {
        this.album = new Album().setId(album.getId()).setTitle(album.getTitle());
        this.users = list;
    }
}
