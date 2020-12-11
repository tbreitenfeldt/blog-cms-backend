package com.timothybreitenfeldt.blog.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.timothybreitenfeldt.blog.dto.PostRequestDto;
import com.timothybreitenfeldt.blog.dto.PostResponseDto;
import com.timothybreitenfeldt.blog.service.PostService;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto createPost(@Valid @RequestBody PostRequestDto postRequestDto) {
        return this.postService.createPost(postRequestDto);
    }

    @GetMapping("/posts/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getAllPosts() {
        return this.postService.getAllPosts();
    }

    @GetMapping("/author/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getPostsForAuthor() {
        return this.postService.getPostsForAuthor();
    }

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto getPost(@PathVariable Long id) {
        return this.postService.getPost(id);
    }

    @PutMapping("/admin/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAnyPost(@PathVariable Long id, @Valid @RequestBody PostRequestDto postRequestDto) {
        this.postService.updatePost(id, postRequestDto);
    }

    @DeleteMapping("/admin/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnyPost(@PathVariable Long id) {
        this.postService.deletePost(id);
    }

}
