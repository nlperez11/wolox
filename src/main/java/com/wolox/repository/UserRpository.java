package com.wolox.repository;

import com.wolox.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRpository extends CrudRepository<User, Integer> {
}
