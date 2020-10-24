package com.wolox.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

@Component
public class HttpHelper {

    @Autowired
    private Environment environment;

    public String getUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(environment.getProperty("external.api") + "users"))
                .GET()
                .build();

        HttpResponse response = client.send(request, BodyHandlers.ofString());
        return response.body().toString();
    }

    public String getAlbums() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(environment.getProperty("external.api") + "albums"))
                .GET()
                .build();

        HttpResponse response = client.send(request, BodyHandlers.ofString());
        return response.body().toString();
    }

}