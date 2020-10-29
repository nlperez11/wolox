package com.wolox.app.models;

import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class AlbumAccessTest {

    private AlbumAccess albumAccess;
    private User user;
    private Album album;
    private Integer id = 1;

    @BeforeEach
    public void init() {
        this.user = new User();
        this.album = new Album();
        albumAccess = new AlbumAccess().setId(id).setUser(user).setAlbum(album).setRead(true).setWrite(false);
    }

    @Test
    public void AlbumAccess() {
        assertNotNull(albumAccess);
    }

    @Test
    public void id() {
        assertEquals(id, albumAccess.getId());
    }

    @Test
    public void user() {
        assertEquals(user, albumAccess.getUser());
    }

    @Test
    public void album() {
        assertEquals(album, albumAccess.getAlbum());
    }

    @Test
    public void read() {
       assertTrue(albumAccess.isRead());
    }

    @Test
    public void write() {
        assertFalse(albumAccess.isWrite());
    }
}
