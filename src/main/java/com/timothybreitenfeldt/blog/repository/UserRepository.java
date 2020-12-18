package com.timothybreitenfeldt.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timothybreitenfeldt.blog.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public abstract Optional<User> findByUsernameOrEmailAllIgnoreCase(String username, String email);

    public default Optional<User> findByUsernameOrEmailAllIgnoreCase(String usernameOrEmail) {
        return this.findByUsernameOrEmailAllIgnoreCase(usernameOrEmail, usernameOrEmail);
    }

}
