package com.wolox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.models.Photo;
import com.wolox.models.User;
import com.wolox.repository.AlbumRepository;
import com.wolox.repository.UserRepository;
import com.wolox.resources.HttpHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SyncService {

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private LocalContainerEntityManagerFactoryBean emf;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(SyncService.class);

    public void synchronizeUsers() throws IOException, InterruptedException {
        JSONArray array = new JSONArray(httpHelper.getUsers());
        EntityManager entityManager = emf.getNativeEntityManagerFactory().createEntityManager();
        logger.info(array.length() + " users to process");
        try {
            entityManager.getTransaction().begin();

            array.forEach(u -> {
                try {
                    User usr = mapper.readValue(u.toString(), User.class);
                    entityManager.persist(usr);
                    logger.info("User " + usr.getId() + " processed");
                } catch (JsonProcessingException e) {
                    logger.error("Error processing the user");
                    logger.error(e.getMessage());
                }
            });

            entityManager.flush();
            entityManager.getTransaction().commit();
            logger.info("End process");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public void synchronizeAlbums() throws IOException, InterruptedException {
        JSONArray array = new JSONArray(httpHelper.getAlbums());
        logger.info(array.length() + " albums to process");

        Map<Integer, List<Album>> map = StreamSupport.stream(array.spliterator(), false)
                .map(a -> {
                    Album album = new Album();
                    JSONObject object = new JSONObject(a.toString());
                    album
                            .setId(object.getInt("id"))
                            .setTitle(object.getString("title"))
                            .setUser(new User().setId(object.getInt("userId")));

                    logger.info("Album " + album.getId() + " processed");
                    return album;
                })
                .collect(Collectors.groupingBy(album -> album.getUser().getId()));

        EntityManager entityManager = emf.getNativeEntityManagerFactory().createEntityManager();
        userRepository.findAllById(map.keySet()).forEach(u -> {
            List<Album> albums = map.get(u.getId());
            if (!albums.isEmpty()) {
                logger.info("storing " + albums.size() + " albums for the user " + u.getId());
                entityManager.getTransaction().begin();
                albums.forEach(a -> {
                    entityManager.persist(a);
                    entityManager.persist(new AlbumAccess().setAlbum(a).setUser(u).setRead(true).setWrite(true));
                });
                entityManager.flush();
                entityManager.getTransaction().commit();
                logger.info("albums for the user " + u.getId() + " stored");
            }
        });

    }

    public void synchronizePhotos() throws IOException, InterruptedException {
        JSONArray array = new JSONArray(httpHelper.getPhotos());
        logger.info(array.length() + " photos to process");

        Map<Integer, List<Photo>> map = StreamSupport.stream(array.spliterator(), false)
                .map(p -> {
                    JSONObject object = new JSONObject(p.toString());
                    Photo photo = new Photo()
                            .setId(object.getInt("id"))
                            .setTitle(object.getString("title"))
                            .setUrl(object.getString("url"))
                            .setThumbnailUrl(object.getString("thumbnailUrl"))
                            .setAlbum(new Album().setId(object.getInt("albumId")));
                    logger.info("Photo " + photo.getUrl() + " processed");
                    return photo;
                })
                .collect(Collectors.groupingBy(photo -> photo.getAlbum().getId()));

        EntityManager entityManager = emf.getNativeEntityManagerFactory().createEntityManager();
        albumRepository.findAllById(map.keySet()).forEach(a -> {
            List<Photo> photos = map.get(a.getId());
            if (!photos.isEmpty()) {
                logger.info("Storing " + photos.size() + " photos for the album " + a.getId());
                entityManager.getTransaction().begin();
                photos.forEach(entityManager::persist);
                entityManager.flush();
                entityManager.getTransaction().commit();
                logger.info("Photos for te album " + a.getId() + " stored");
            }
        });

    }

}
