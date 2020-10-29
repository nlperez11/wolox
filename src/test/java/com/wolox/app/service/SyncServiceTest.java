package com.wolox.app.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.wolox.models.Album;
import com.wolox.models.User;
import com.wolox.repository.AlbumRepository;
import com.wolox.repository.UserRepository;
import com.wolox.resources.HttpHelper;
import com.wolox.service.SyncService;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class SyncServiceTest {

    @Mock
    private HttpHelper httpHelper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private LocalContainerEntityManagerFactoryBean emf;

    @Mock
    private EntityManagerFactory factory;

    @Mock
    private EntityManager em;

    @Mock
    private EntityTransaction transaction;

    @InjectMocks
    private SyncService syncService;

    private static WireMockServer wireMockServer;

    private String mockUsers = new com.wolox.app.mock.Mock().users();
    private String mockAlbums = new com.wolox.app.mock.Mock().albums();
    private String mockPhotos = new com.wolox.app.mock.Mock().photos();

    @BeforeAll
    public static void initMockServer() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @AfterAll
    public static void stopMockServer() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(emf.getNativeEntityManagerFactory()).thenReturn(factory);
        when(emf.getNativeEntityManagerFactory().createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(transaction);
    }

    @Test
    public void syncService() {
        assertNotNull(syncService);
    }

    @Test
    public void synchronizeUsers() throws IOException, InterruptedException {
        when(httpHelper.getUsers()).thenReturn(mockUsers);

        String url = "https://jsonplaceholder.typicode.com/";
        stubFor(get(urlEqualTo(url + "users"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(mockUsers)));

        syncService.synchronizeUsers();
        verify(em, times(2)).persist(any());
        verify(transaction, times(1)).begin();
        verify(transaction, times(1)).commit();
    }

    @Test
    public void synchronizeAlbums() throws IOException, InterruptedException {
        when(httpHelper.getAlbums()).thenReturn(mockAlbums);
        when(userRepository.findAllById(any())).thenReturn(this.getUsers());

        String url = "https://jsonplaceholder.typicode.com/";
        stubFor(get(urlEqualTo(url + "albums"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(mockAlbums)));

        syncService.synchronizeAlbums();
        verify(em, times(28)).persist(any());
        verify(transaction, times(2)).begin();
        verify(transaction, times(2)).commit();
    }

    @Test
    public void synchronizePhotos() throws IOException, InterruptedException {
        when(httpHelper.getPhotos()).thenReturn(mockPhotos);
        when(albumRepository.findAllById(any())).thenReturn(this.getAlbums());

        String url = "https://jsonplaceholder.typicode.com/";
        stubFor(get(urlEqualTo(url + "photos"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(mockPhotos)));

        syncService.synchronizePhotos();
        verify(em, times(9)).persist(any());
        verify(transaction, times(1)).begin();
        verify(transaction, times(1)).commit();
    }

    private Iterable<User> getUsers() {
        User[] users = new User[2];

        IntStream.range(0, 2)
                .forEach(i -> users[i] = new User().setId(i + 1));

        return IterableUtil.iterable(users);
    }

    private Iterable<Album> getAlbums() {
        Album[] albums = new Album[1];

        IntStream.range(0, 1)
                .forEach(i -> albums[i] = new Album().setId(i + 1));

        return IterableUtil.iterable(albums);
    }
}
