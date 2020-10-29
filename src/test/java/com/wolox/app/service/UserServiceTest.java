package com.wolox.app.service;

import com.wolox.models.User;
import com.wolox.repository.UserRepository;
import com.wolox.service.UserService;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void userService() {
        assertNotNull(service);
    }

    @Test
    public void getUsers() {
        Iterable<User> iterable = IterableUtil.iterable(new User().setId(1), new User().setId(2));
        when(userRepository.findAll()).thenReturn(iterable);
        assertEquals(iterable, service.getUsers());
    }

    @Test
    public void getUsersEmpty() {
        Iterable<User> iterable = IterableUtil.iterable();
        when(userRepository.findAll()).thenReturn(iterable);
        assertEquals(iterable, service.getUsers());
    }


}
