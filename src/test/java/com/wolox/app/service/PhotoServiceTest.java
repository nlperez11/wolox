package com.wolox.app.service;

import com.wolox.models.Album;
import com.wolox.models.Photo;
import com.wolox.repository.PhotoRepository;
import com.wolox.service.PhotoService;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class PhotoServiceTest {

    @Mock
    private PhotoRepository repository;

    @InjectMocks
    private PhotoService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void photoService() {
        assertNotNull(service);
    }

    @Test
    public void getPhotos() {
        Iterable<Photo> iterable = this.getPhotosIterable();
        when(repository.findAll()).thenReturn(iterable);
        assertEquals(iterable, service.getPhotos());
    }

    @Test
    public void getPhotosEmpty() {
        Iterable<Photo> iterable = IterableUtil.iterable();
        when(repository.findAll()).thenReturn(iterable);
        assertEquals(iterable, service.getPhotos());
    }

    @Test
    public void getPhotosByUser() {
        Integer id = Mockito.any();
        Iterable<Photo> iterable = this.getPhotosIterable();
        when(repository.findAllByAlbum_User_Id(id)).thenReturn(iterable);
        assertEquals(iterable, service.getPhotosByUser(id));
    }

    @Test
    public void getPhotosByUserEmpty() {
        Integer id = Mockito.any();
        Iterable<Photo> iterable = IterableUtil.iterable();
        when(repository.findAllByAlbum_User_Id(id)).thenReturn(iterable);
        assertEquals(iterable, service.getPhotosByUser(id));
    }

    private Iterable<Photo> getPhotosIterable() {
        int size = 10;
        Photo[] photos = new Photo[size];

        IntStream.range(0, size)
                .forEach(i -> photos[i] = new Photo()
                        .setId(i)
                        .setAlbum(new Album())
                        .setUrl("url" + i + ".com")
                        .setThumbnailUrl("thumb" + i + ".com")
                        .setTitle("title" + i));

        return IterableUtil.iterable(photos);
    }

}
