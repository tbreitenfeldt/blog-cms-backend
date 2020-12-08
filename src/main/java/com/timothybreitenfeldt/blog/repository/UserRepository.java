package com.timothybreitenfeldt.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.timothybreitenfeldt.blog.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("FROM user u WHERE (u.username = :identifier or u.email = :identifier) and u.role = 'ROLE_ADMINISTRATOR'")
    public abstract Optional<User> findAdministratorByUsernameOrEmail(String identifier);

    @Query("FROM user u WHERE (u.username = :identifier or u.email = :identifier) and u.role = 'ROLE_AUTHOR'")
    public abstract Optional<User> findAuthorByUsernameOrEmail(String identifier);

}
