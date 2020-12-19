package com.timothybreitenfeldt.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timothybreitenfeldt.blog.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public abstract List<Post> findAllByUserId(Long userId);

    public abstract List<Post> findByTitleContaining(String title);

    public abstract void deleteByIdAndUserId(Long id, Long userId);

}
