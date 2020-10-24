package com.wolox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.models.Album;
import com.wolox.models.User;
import com.wolox.repository.UserRpository;
import com.wolox.resources.HttpHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private LocalContainerEntityManagerFactoryBean emf;

    @Autowired
    private UserRpository userRpository;

    private ObjectMapper mapper = new ObjectMapper();

    public void getUsers() throws IOException, InterruptedException {
        httpHelper.getUsers();
    }

    public void synchronizeUsers() throws IOException, InterruptedException {
        JSONArray array = new JSONArray(httpHelper.getUsers());
        EntityManager entityManager = emf.getNativeEntityManagerFactory().createEntityManager();
        System.out.println(array.length() + " users to process");
        try {
            entityManager.getTransaction().begin();

            array.forEach(u -> {
                try {
                    User usr = mapper.readValue(u.toString(), User.class);
                    entityManager.persist(usr);
                    System.out.println("User " + usr.getId() + " processed");
                } catch (JsonProcessingException e) {
                    System.out.println(e.getMessage());
                }
            });

            entityManager.flush();
            entityManager.getTransaction().commit();
            System.out.println("End process");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void synchronizeAlbums() throws IOException, InterruptedException {
        JSONArray array = new JSONArray(httpHelper.getAlbums());
        System.out.println(array.length() + " albums to process");

        Map<Integer, List<Album>> map = StreamSupport.stream(array.spliterator(), false)
                .map(a -> {
                    Album album = new Album();
                    JSONObject object = new JSONObject(a.toString());
                    album
                            .setId(object.getInt("id"))
                            .setTitle(object.getString("title"))
                            .setUser(new User().setId(object.getInt("userId")));

                    System.out.println("Album " + album.getId() + " processed");
                    return album;
                })
                .collect(Collectors.groupingBy(album -> album.getUser().getId()));

        EntityManager entityManager = emf.getNativeEntityManagerFactory().createEntityManager();
        userRpository.findAllById(map.keySet()).forEach(u -> {
            List<Album> albums = map.get(u.getId());
            if (!albums.isEmpty()) {
                System.out.println("saving " + albums.size() + " albums for the user " + u.getId());
                entityManager.getTransaction().begin();
                albums.forEach(entityManager::persist);
                entityManager.flush();
                entityManager.getTransaction().commit();
                System.out.println("albums for the user " + u.getId() + " saved");
            }
        });

    }

}