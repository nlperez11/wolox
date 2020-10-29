package com.wolox.app.models;

import com.wolox.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserTest {

    private User user;
    private Integer id = 1;
    private String username = "username", phone = "123456789", webSite = "website.com", email = "email@email.com";

    @BeforeEach
    public void init() {
        this.user = new User().setId(id).setName(username).setEmail(email).setPhone(phone).setWebsite(webSite).setUsername(username);
    }

    @Test
    public void user() {
        assertNotNull(user);
    }

    @Test
    public void id() {
        assertEquals(id, user.getId());
    }

    @Test
    public void username() {
        assertEquals(username, user.getUsername());
    }

    @Test
    public void phone() {
        assertEquals(phone, user.getPhone());
    }

    @Test
    public void webSite() {
        assertEquals(webSite, user.getWebsite());
    }

    @Test
    public void email() {
        assertEquals(email, user.getEmail());
    }
}

