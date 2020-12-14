package com.timothybreitenfeldt.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.timothybreitenfeldt.blog.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p.id, p.title, p.updatedOn, p.user.username FROM Post p WHERE p.user.id = :userId")
    public abstract List<Post> findAllPostHeadersByUserId(Long userId);

    @Query("SELECT p.id, p.title, p.updatedOn, p.user.username FROM Post p")
    public abstract List<Post> findAllPostHeaders();

    @Query("DELETE FROM Post WHERE id = :id and user.id = :userId")
    public abstract void deleteByIdForUser(Long id, Long userId);

}
