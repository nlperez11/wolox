package com.wolox.service;

import com.wolox.resources.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class CommentService {

    @Autowired
    private HttpHelper httpHelper;

    public String getComments(String name, String email) throws IOException, InterruptedException {
        if (Objects.nonNull(name))
            return httpHelper.getComments("name", name.replace(" ", "%20"));

        if (Objects.nonNull(email))
            return httpHelper.getComments("email", email);

        return "[]";
    }
}
