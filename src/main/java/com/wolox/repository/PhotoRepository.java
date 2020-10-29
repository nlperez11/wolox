package com.wolox.repository;

import com.wolox.models.Photo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Integer> {

    Iterable<Photo> findAllByAlbum_User_Id(Integer id);
}
