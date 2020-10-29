package com.wolox.app.models;

import com.wolox.models.Album;
import com.wolox.models.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class PhotoTest {

    private Photo photo;
    private Album album;
    private Integer id = 1;
    private String title = "title", url = "url.com", thumbnail = "thumb-url.com";

    @BeforeEach
    public void init() {
        this.album = new Album();
        this.photo = new Photo().setId(id).setTitle(title).setUrl(url).setAlbum(album).setThumbnailUrl(thumbnail);
    }

    @Test
    public void photo() {
        assertNotNull(photo);
    }

    @Test
    public void id() {
        assertEquals(id, photo.getId());
    }

    @Test
    public void title() {
        assertEquals(title, photo.getTitle());
    }

    @Test
    public void url() {
        assertEquals(url, photo.getUrl());
    }

    @Test
    public void thumbNailUrl() {
        assertEquals(thumbnail, photo.getThumbnailUrl());
    }

    @Test
    public void album() {
        assertEquals(album, photo.getAlbum());
    }
}
