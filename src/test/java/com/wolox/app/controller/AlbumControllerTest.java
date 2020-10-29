package com.wolox.app.controller;

import com.google.gson.Gson;
import com.wolox.controller.AlbumController;
import com.wolox.controller.Exception.ErrorHandler;
import com.wolox.controller.validator.AlbumAccessValidator;
import com.wolox.dto.AlbumAccessDTO;
import com.wolox.dto.AlbumAccessListDTO;
import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.models.User;
import com.wolox.service.AlbumService;
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

import java.util.Collections;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AlbumController.class, AlbumControllerTest.class, ErrorHandler.class})
@WebMvcTest(value = AlbumController.class)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    private AlbumAccess albumAccess;
    private User user;
    private Album album;
    private String url = "http://localhost:8080/api/album/access";
    private Gson json = new Gson();

    @BeforeEach
    public void init() {
        this.user = new User().setId(1).setEmail("mail").setName("name").setUsername("username").setWebsite("web").setPhone("123");
        this.album = new Album().setId(1).setUser(user).setTitle("title");
        this.albumAccess = new AlbumAccess().setId(1).setWrite(true).setRead(false).setAlbum(album).setUser(user);
        MockitoAnnotations.initMocks(this);
    }

    //TODO fail cases

    @Test
    public void albumService() {
        assertNotNull(albumService);
    }

    @Test
    public void getUsersByPermissionAlbum() throws Exception {

        AlbumAccessDTO albumAccessDTO = new AlbumAccessDTO()
                .setId(albumAccess.getId())
                .setRead(albumAccess.isRead()).setWrite(albumAccess.isWrite())
                .setUser(album.getUser());
        AlbumAccessListDTO dto = new AlbumAccessListDTO(album, Collections.singletonList(albumAccessDTO));

        when(albumService.getUsersByAlbumPermissions(album.getId(), "read")).thenReturn(dto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url + "/user?album=" + album.getId() + "&permission=read")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json.toJson(this.buildBody(null)));

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(json.toJson(dto), res.getResponse().getContentAsString()));
    }

    @Test
    public void createSharedAlbum() throws Exception {
        when(albumService.createSharedAlbum(any())).thenReturn(albumAccess);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(url)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json.toJson(this.buildBody(null)));

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(json.toJson(albumAccess), res.getResponse().getContentAsString()));
    }

    @Test
    public void updateSharedAlbum() throws Exception {
        when(albumService.updateSharedAlbum(any())).thenReturn(albumAccess);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(url)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(json.toJson(this.buildBody(1)));

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(json.toJson(albumAccess), res.getResponse().getContentAsString()));
    }

    private AlbumAccessValidator buildBody(Integer id) {
        AlbumAccessValidator body = new AlbumAccessValidator()
                .setId(1)
                .setAlbumId(album.getId())
                .setRead(albumAccess.isRead())
                .setWrite(albumAccess.isWrite())
                .setUserId(user.getId());
        return Objects.isNull(id) ? body : body.setId(id);
    }
}
