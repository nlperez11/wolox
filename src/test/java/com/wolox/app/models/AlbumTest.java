package com.wolox.app.models;

import com.wolox.models.Album;
import com.wolox.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class AlbumTest {

    private Album album;
    private User user;
    private Integer id = 1;
    private String title = "title";

    @BeforeEach
    public void init() {
        this.user = new User();
        this.album = new Album().setTitle(title).setId(id).setUser(user);
    }

    @Test
    public void album() {
        assertNotNull(album);
    }

    @Test
    public void id() {
        assertEquals(album.getId(), id);
    }

    @Test
    public void name() {
        assertEquals(album.getTitle(), title);
    }

    @Test
    public void user() {
        assertEquals(album.getUser(), user);
    }
}
