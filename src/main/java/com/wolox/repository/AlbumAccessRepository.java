package com.wolox.repository;

import com.wolox.models.AlbumAccess;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumAccessRepository extends CrudRepository<AlbumAccess, Integer> {
}
