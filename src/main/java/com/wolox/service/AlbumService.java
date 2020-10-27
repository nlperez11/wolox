package com.wolox.service;

import com.wolox.controller.Exception.AlbumAccessException;
import com.wolox.dto.AlbumAccessDTO;
import com.wolox.dto.AlbumAccessListDTO;
import com.wolox.models.Album;
import com.wolox.models.AlbumAccess;
import com.wolox.repository.AlbumAccessRepository;
import com.wolox.repository.AlbumRepository;
import com.wolox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumAccessRepository albumAccessRepository;

    @Autowired
    private LocalContainerEntityManagerFactoryBean emf;

    public Iterable<Album> getAlbums() {
        return repository.findAll();
    }

    public AlbumAccessListDTO getUsersByAlbumPermissions(Integer albumId, String permission) {
        EntityManager em = emf.getNativeEntityManagerFactory().createEntityManager();

        CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
        CriteriaQuery<AlbumAccess> query = queryBuilder.createQuery(AlbumAccess.class);
        Root<AlbumAccess> root = query.from(AlbumAccess.class);

        Predicate[] predicates = new Predicate[2];
        predicates[0] = queryBuilder.equal(root.get("album").get("id"), albumId);
        predicates[1] = queryBuilder.and(queryBuilder.isTrue(root.get(permission)));

        query.select(root).where(predicates).orderBy(queryBuilder.asc(root.get("user").get("id")));
        List<AlbumAccess> list = em.createQuery(query).getResultList();
        AlbumAccessListDTO dto = new AlbumAccessListDTO();

        if (!list.isEmpty())
            dto = new AlbumAccessListDTO(
                    list.get(0).getAlbum(),
                    list.stream().map(AlbumAccessDTO::new).collect(Collectors.toList())
            );

        return dto;
    }

    public AlbumAccess createSharedAlbum(AlbumAccess albumAccess) throws AlbumAccessException {

        this.checkUser(albumAccess);
        this.checkAlbum(albumAccess);

        return albumAccessRepository.save(albumAccess);
    }

    public AlbumAccess updateSharedAlbum(AlbumAccess albumAccess) throws AlbumAccessException {
        AlbumAccess aa = albumAccessRepository.findByUser_IdAndAlbum_Id(
                albumAccess.getUser().getId(),
                albumAccess.getAlbum().getId()
        );

        if (Objects.isNull(aa))
            throw new AlbumAccessException("Register not found with userId " + albumAccess.getUser().getId() +
                    " and albumId, " + albumAccess.getAlbum().getId());

        return albumAccessRepository.save(aa.setWrite(albumAccess.isWrite()).setRead(albumAccess.isRead()));
    }

    private AlbumAccess checkUser(AlbumAccess albumAccess) throws AlbumAccessException {
        return userRepository.findById(albumAccess.getUser().getId())
                .map(albumAccess::setUser)
                .orElseThrow(() -> new AlbumAccessException("User not found, " + albumAccess.getUser().getId()));
    }

    private AlbumAccess checkAlbum(AlbumAccess albumAccess) throws AlbumAccessException {
        return repository.findById(albumAccess.getAlbum().getId())
                .map(albumAccess::setAlbum)
                .orElseThrow(() -> new AlbumAccessException("Album not found, " + albumAccess.getAlbum().getId()));
    }
}
