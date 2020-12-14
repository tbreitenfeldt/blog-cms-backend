package com.timothybreitenfeldt.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.timothybreitenfeldt.blog.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("FROM User u WHERE lower(u.username) like lower(:identifier) or lower(u.email) like lower(:identifier)")
    public abstract Optional<User> findUserByUsernameOrEmail(String identifier);

}
