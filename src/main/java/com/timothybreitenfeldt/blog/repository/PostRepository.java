package com.timothybreitenfeldt.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.timothybreitenfeldt.blog.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("FROM Post p WHERE p.user.username = :username")
    public abstract List<Post> findAllByUsername(String username);

}
