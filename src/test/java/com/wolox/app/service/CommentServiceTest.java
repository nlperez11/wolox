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
    private String mockResponse = "[" +
            "[\n" +
            "  {\n" +
            "    \"postId\": 1,\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"id labore ex et quam laborum\",\n" +
            "    \"email\": \"Eliseo@gardner.biz\",\n" +
            "    \"body\": \"laudantium enim quasi est quidem magnam voluptate ipsam eos\\ntempora quo necessitatibus\\ndolor quam autem quasi\\nreiciendis et nam sapiente accusantium\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"postId\": 1,\n" +
            "    \"id\": 2,\n" +
            "    \"name\": \"quo vero reiciendis velit similique earum\",\n" +
            "    \"email\": \"Jayne_Kuhic@sydney.com\",\n" +
            "    \"body\": \"est natus enim nihil est dolore omnis voluptatem numquam\\net omnis occaecati quod ullam at\\nvoluptatem error expedita pariatur\\nnihil sint nostrum voluptatem reiciendis et\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"postId\": 1,\n" +
            "    \"id\": 3,\n" +
            "    \"name\": \"odio adipisci rerum aut animi\",\n" +
            "    \"email\": \"Nikita@garfield.biz\",\n" +
            "    \"body\": \"quia molestiae reprehenderit quasi aspernatur\\naut expedita occaecati aliquam eveniet laudantium\\nomnis quibusdam delectus saepe quia accusamus maiores nam est\\ncum et ducimus et vero voluptates excepturi deleniti ratione\"\n" +
            "  }" +
            "]";

    @BeforeAll
    public static void initMockServer() {
        System.out.println("START MOCK SERVER");
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @AfterAll
    public static void stopMockServer() {
        System.out.println("stop mock server");
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

        stubFor(get(urlEqualTo(environment.getProperty("external.api") + "?name=" + name))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(mockResponse)));

        assertEquals(mockResponse, service.getComments(name, email));
    }

    @Test
    public void getCommentByEmail() throws IOException, InterruptedException {
        when(httpHelper.getComments("email", email)).thenReturn(mockResponse);

        stubFor(get(urlEqualTo(environment.getProperty("external.api") + "?email=" + email))
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
