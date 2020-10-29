package com.wolox.app.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.wolox.resources.HttpHelper;
import com.wolox.service.CommentService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommentService.class, CommentServiceTest.class, HttpHelper.class})
@WebMvcTest(value = CommentService.class)
public class CommentServiceTest {

    @Autowired
    private Environment environment;

    @Mock
    private HttpHelper httpHelper;

    @InjectMocks
    private CommentService service;

    private static WireMockServer wireMockServer;

    private String name = "name", email = "";
    private static String mockResponse = new com.wolox.app.mock.Mock().comments();

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
    }

    @Test
    public void commentService() {
        assertNotNull(service);
    }

    @Test
    public void getCommentByName() throws IOException, InterruptedException {
        when(httpHelper.getComments("name", name)).thenReturn(mockResponse);

        stubFor(get(urlEqualTo(environment.getProperty("external.api") + "comments?name=" + name))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(mockResponse)));

        assertEquals(mockResponse, service.getComments(name, email));
    }

    @Test
    public void getCommentByEmail() throws IOException, InterruptedException {
        when(httpHelper.getComments("email", email)).thenReturn(mockResponse);

        stubFor(get(urlEqualTo(environment.getProperty("external.api") + "comments?email=" + email))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(mockResponse)));

        assertEquals(mockResponse, service.getComments(null, email));
    }

    @Test
    public void getCommentsEmpty() throws IOException, InterruptedException {
        assertEquals("[]", service.getComments(null, null));
    }

}
