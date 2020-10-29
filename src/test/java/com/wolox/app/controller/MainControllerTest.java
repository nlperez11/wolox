package com.wolox.app.controller;

import com.google.gson.Gson;
import com.wolox.controller.Exception.ErrorHandler;
import com.wolox.controller.MainController;
import com.wolox.models.Album;
import com.wolox.models.Photo;
import com.wolox.models.User;
import com.wolox.service.AlbumService;
import com.wolox.service.PhotoService;
import com.wolox.service.UserService;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainController.class, MainControllerTest.class, ErrorHandler.class})
@WebMvcTest(value = MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AlbumService albumService;

    @MockBean
    private PhotoService photoService;

    private String url = "http://localhost:8080/api";
    private Gson json = new Gson();

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void photoService() {
        assertNotNull(photoService);
    }

    @Test
    public void userService() {
        assertNotNull(userService);
    }

    @Test
    public void albumService() {
        assertNotNull(albumService);
    }

    @Test
    public void getUsers() throws Exception {

        Iterable<User> users = this.usersMock();
        when(userService.getUsers()).thenReturn(users);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/user")
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(json.toJson(users), res.getResponse().getContentAsString()));
    }

    @Test
    public void getUsersEmpty() throws Exception {

        when(userService.getUsers()).thenReturn(IterableUtil.iterable());

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/user")
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals("[]", res.getResponse().getContentAsString()));
    }

    @Test
    public void getAlbums() throws Exception {

        Iterable<Album> albums = this.albumsMock();
        when(albumService.getAlbums()).thenReturn(albums);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/album")
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(json.toJson(albums), res.getResponse().getContentAsString()));
    }

    @Test
    public void getAlbumsEmpty() throws Exception {

        Iterable<Album> albums = IterableUtil.iterable();
        when(albumService.getAlbums()).thenReturn(albums);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/album")
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals("[]", res.getResponse().getContentAsString()));
    }

    @Test
    public void getPhotos() throws Exception {

        Iterable<Photo> photos = this.photosMock();
        when(photoService.getPhotos()).thenReturn(photos);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/photo")
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(json.toJson(photos), res.getResponse().getContentAsString()));
    }

    @Test
    public void getPhotosEmpty() throws Exception {

        Iterable<Photo> photos = IterableUtil.iterable();
        when(photoService.getPhotos()).thenReturn(photos);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/photo")
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals("[]", res.getResponse().getContentAsString()));
    }

    @Test
    public void getPhotosByUser() throws Exception {

        Iterable<Photo> photos = this.photosMock();
        when(photoService.getPhotosByUser(any())).thenReturn(photos);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/photo/user/" + 1)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(json.toJson(photos), res.getResponse().getContentAsString()));
    }

    @Test
    public void getPhotosByUserEmpty() throws Exception {

        Iterable<Photo> photos = IterableUtil.iterable();
        when(photoService.getPhotosByUser(any())).thenReturn(photos);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/photo/user/" + 1)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals("[]", res.getResponse().getContentAsString()));
    }

    private Iterable<User> usersMock() {
        User[] users = new User[10];

        IntStream
                .range(0, 10)
                .forEach(i -> users[i] = new User()
                        .setId(i)
                        .setUsername("username" + i)
                        .setEmail("email" + i)
                        .setPhone("1234567" + i)
                        .setWebsite("website" + i)
                        .setName("name" + i));

        return IterableUtil.iterable(users);
    }

    private Iterable<Album> albumsMock() {
        Album[] albums = new Album[10];

        IntStream
                .range(0, 10)
                .forEach(i -> albums[i] = new Album()
                        .setId(i)
                        .setTitle("title" + i)
                        .setUser(new User()
                                .setId(i)
                                .setName("user" + 1)
                                .setEmail("email" + i)
                                .setUsername("username" + i)
                                .setPhone("123436" + i)
                                .setWebsite("website" + i)));

        return IterableUtil.iterable(albums);
    }

    private Iterable<Photo> photosMock() {
        Photo[] Photos = new Photo[10];

        IntStream
                .range(0, 10)
                .forEach(i -> Photos[i] = new Photo()
                        .setId(i)
                        .setTitle("title" + i)
                        .setUrl("url" + i)
                        .setThumbnailUrl("url" + i)
                        .setAlbum(new Album()
                                .setId(i)
                                .setTitle("title" + i)));

        return IterableUtil.iterable(Photos);
    }


}
