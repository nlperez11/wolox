package com.wolox.app.service;

import com.wolox.controller.Exception.AlbumAccessException;
import com.wolox.dto.AlbumAccessDTO;
import com.wolox.dto.AlbumAccessListDTO;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Mock
    private EntityManagerFactory factory;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private CriteriaQuery query;

    @Mock
    private Predicate predicate;

    @Mock
    private Root root;

    @Mock
    private Path path;

    @Mock
    private Order order;

    @Mock
    private Expression expression;

    @Mock
    private TypedQuery persistQuery;

    @Mock
    private EntityManager em;

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
        this.albumAccess = new AlbumAccess().setId(1).setAlbum(album).setUser(user).setRead(true).setWrite(false);
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

        List<AlbumAccess> list = Arrays.asList(albumAccess, albumAccess, albumAccess, albumAccess);
        when(emf.getNativeEntityManagerFactory()).thenReturn(factory);
        when(emf.getNativeEntityManagerFactory().createEntityManager()).thenReturn(em);
        when(em.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(AlbumAccess.class)).thenReturn(query);
        when(query.from(AlbumAccess.class)).thenReturn(root);
        when(builder.equal(expression, album.getId())).thenReturn(predicate);
        when(builder.and(predicate)).thenReturn(predicate);
        when(root.get(anyString())).thenReturn(path);


        when(query.select(root)).thenReturn(query);
        when(query.where(predicate)).thenReturn(query);
        when(builder.asc(expression)).thenReturn(order);
        when(query.orderBy(order)).thenReturn(query);
        when(em.createQuery(query)).thenReturn(persistQuery);
        when(persistQuery.getResultList()).thenReturn(list);

        AlbumAccessListDTO dto = new AlbumAccessListDTO(
                album,
                list.stream().map(AlbumAccessDTO::new).collect(Collectors.toList())
        );

        assertEquals(dto, albumService.getUsersByAlbumPermissions(album.getId(), "read"));
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
