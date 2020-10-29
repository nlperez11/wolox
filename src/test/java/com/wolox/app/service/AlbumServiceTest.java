package com.wolox.app.service;

import com.wolox.controller.Exception.AlbumAccessException;
import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.models.User;
import com.wolox.repository.AlbumAccessRepository;
import com.wolox.repository.AlbumRepository;
import com.wolox.repository.UserRepository;
import com.wolox.service.AlbumService;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AlbumAccessRepository albumAccessRepository;

    @Mock
    private LocalContainerEntityManagerFactoryBean emf;

    @InjectMocks
    private AlbumService albumService;

    private User user;
    private Album album;
    private AlbumAccess albumAccess;
    private Integer id = 1;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.user = new User().setId(id);
        this.album = new Album().setId(id);
        this.albumAccess = new AlbumAccess().setAlbum(album).setUser(user).setRead(true).setWrite(false);
    }

    @Test
    public void albumService() {
        assertNotNull(albumService);
    }

    @Test
    public void getAlbums() {
        Iterable<Album> iterable = this.getAlbumsIterable();
        when(albumRepository.findAll()).thenReturn(iterable);
        assertEquals(iterable, albumService.getAlbums());
    }

    @Test
    public void getAlbumsEmpty() {
        Iterable<Album> iterable = IterableUtil.iterable();
        when(albumRepository.findAll()).thenReturn(iterable);

        assertEquals(iterable, albumService.getAlbums());
    }

    @Test
    public void getUsersByAlbumPermissions() {
        //TODO
    }

    @Test
    public void createSharedAlbum() throws AlbumAccessException {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(albumRepository.findById(album.getId())).thenReturn(Optional.of(album));
        when(albumAccessRepository.save(albumAccess)).thenReturn(albumAccess);

        assertEquals(albumAccess, albumService.createSharedAlbum(albumAccess));
    }

    @Test()
    public void createSharedAlbumUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(AlbumAccessException.class, () -> albumService.createSharedAlbum(albumAccess));
    }

    @Test
    public void createSharedAlbumNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(albumRepository.findById(album.getId())).thenReturn(Optional.empty());

        assertThrows(AlbumAccessException.class, () -> albumService.createSharedAlbum(albumAccess));
    }

    @Test
    public void updateSharedAlbum() throws AlbumAccessException {
        AlbumAccess aa = new AlbumAccess().setId(id).setAlbum(album).setUser(user).setRead(false).setWrite(false);

        when(albumAccessRepository.findByUser_IdAndAlbum_Id(aa.getUser().getId(), aa.getAlbum().getId())).thenReturn(albumAccess);
        when(albumAccessRepository.save(albumAccess)).thenReturn(aa);

        assertEquals(aa, albumService.updateSharedAlbum(aa));
    }

    @Test
    public void updateSharedAlbumNotFound() {
        when(albumAccessRepository.findByUser_IdAndAlbum_Id(user.getId(), album.getId())).thenReturn(null);
        assertThrows(AlbumAccessException.class, () -> albumService.updateSharedAlbum(albumAccess));
    }


    private Iterable<Album> getAlbumsIterable() {
        int size = 10;
        Album[] albums = new Album[size];

        IntStream.range(0, size)
                .forEach(i -> albums[i] = new Album()
                        .setId(i)
                        .setTitle("title" + i)
                        .setUser(new User()));

        return IterableUtil.iterable(albums);
    }

}
