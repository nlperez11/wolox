package com.wolox.app.controller;

import com.wolox.app.mock.Mock;
import com.wolox.controller.CommentController;
import com.wolox.controller.Exception.ErrorHandler;
import com.wolox.service.CommentService;
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

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommentController.class, CommentControllerTest.class, ErrorHandler.class})
@WebMvcTest(value = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService service;

    private String url = "http://localhost:8080/api/comment";
    private String commentsMock = new Mock().comments();

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getComments() throws Exception {

        when(service.getComments(any(), any())).thenReturn(commentsMock);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals(commentsMock, res.getResponse().getContentAsString()));
    }

    @Test
    public void getCommentsEmpty() throws Exception {

        when(service.getComments(any(), any())).thenReturn("[]");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk())
                .andExpect(res -> assertEquals("[]", res.getResponse().getContentAsString()));
    }

    @Test
    public void getCommentsServiceUnavailable() throws Exception {

        when(service.getComments(any(), any())).thenThrow(new IOException("error"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(url)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isServiceUnavailable());
    }
}
