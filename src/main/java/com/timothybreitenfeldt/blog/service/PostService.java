package com.timothybreitenfeldt.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.timothybreitenfeldt.blog.dto.PostHeaderResponseDto;
import com.timothybreitenfeldt.blog.dto.PostRequestDto;
import com.timothybreitenfeldt.blog.dto.PostResponseDto;
import com.timothybreitenfeldt.blog.exception.PostNotFoundException;
import com.timothybreitenfeldt.blog.exception.UserNotAuthenticatedException;
import com.timothybreitenfeldt.blog.model.Post;
import com.timothybreitenfeldt.blog.model.User;
import com.timothybreitenfeldt.blog.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Post postModel = this.mapFromPostRequestDtoToPostModel(postRequestDto);
        Post result = this.postRepository.save(postModel);
        return this.mapFromPostModelToPostResponseDto(result);
    }

    public List<PostHeaderResponseDto> getAllPostHeaders() {
        List<Post> posts = this.postRepository.findAllPostHeaders();
        return posts.stream().map(this::mapFromPostModelToPostHeaderResponseDto).collect(Collectors.toList());
    }

    public List<PostHeaderResponseDto> getPostHeadersForAuthor() {
        String username = this.getUsernameFromSecurityContext();
        List<Post> posts = this.postRepository.findAllPostHeadersByUsername(username);
        return posts.stream().map(this::mapFromPostModelToPostHeaderResponseDto).collect(Collectors.toList());
    }

    public PostResponseDto getPost(Long id) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " cannot be found"));
        return this.mapFromPostModelToPostResponseDto(post);
    }

    @Transactional
    public void updateAnyPost(Long id, PostRequestDto postRequestDto) {
        if (!this.postRepository.existsById(id)) {
            throw new PostNotFoundException("Post with ID " + id + " cannot be found.");
        }

        boolean isUserRequest = false;
        Post post = this.mapFromPostRequestDtoToPostModel(id, postRequestDto, isUserRequest);
        this.postRepository.save(post);
    }

    @Transactional
    public void updatePostForUser(Long id, PostRequestDto postRequestDto) {
        if (!this.postRepository.existsById(id)) {
            throw new PostNotFoundException("Post with ID " + id + " cannot be found.");
        }

        Post post = this.mapFromPostRequestDtoToPostModel(id, postRequestDto);
        this.postRepository.save(post);
    }

    @Transactional
    public void deleteAnyPost(Long id) {
        if (!this.postRepository.existsById(id)) {
            throw new PostNotFoundException("Post with ID " + id + " cannot be found.");
        }

        this.postRepository.deleteById(id);
    }

    @Transactional
    public void deletePostForUser(Long id) {
        if (!this.postRepository.existsById(id)) {
            throw new PostNotFoundException("Post with ID " + id + " cannot be found.");
        }

        String username = this.getUsernameFromSecurityContext();
        this.postRepository.deleteByIdForUser(id, username);
    }

    private Post mapFromPostRequestDtoToPostModel(PostRequestDto postRequestDto) {
        return this.mapFromPostRequestDtoToPostModel(null, postRequestDto, true);
    }

    private Post mapFromPostRequestDtoToPostModel(Long id, PostRequestDto postRequestDto) {
        boolean includeUsername = true;
        return this.mapFromPostRequestDtoToPostModel(id, postRequestDto, includeUsername);
    }

    private Post mapFromPostRequestDtoToPostModel(Long id, PostRequestDto postRequestDto, boolean includeUsername) {
        Post post = new Post();
        User user = null;

        if (id != null) {
            post.setId(id);
        }

        if (includeUsername) {
            System.out.println("hello world");
            user = new User();
            String username = this.getUsernameFromSecurityContext();
            System.out.println("username: " + username);
            user.setUsername(username);
        }

        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setUser(user);
        return post;
    }

    private PostHeaderResponseDto mapFromPostModelToPostHeaderResponseDto(Post post) {
        PostHeaderResponseDto postHeaderResponseDto = new PostHeaderResponseDto();

        postHeaderResponseDto.setId(post.getId());
        postHeaderResponseDto.setTitle(post.getTitle());
        postHeaderResponseDto.setUsername(post.getUser().getUsername());
        postHeaderResponseDto.setUpdatedOn(post.getUpdatedOn());
        return postHeaderResponseDto;
    }

    private PostResponseDto mapFromPostModelToPostResponseDto(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto();

        postResponseDto.setId(post.getId());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setUsername(post.getUser().getUsername());
        postResponseDto.setCreatedOn(post.getCreatedOn());
        postResponseDto.setUpdatedOn(post.getUpdatedOn());
        return postResponseDto;
    }

    private String getUsernameFromSecurityContext() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.isEmpty()) {
            throw new UserNotAuthenticatedException("Unable to retrieve username.");
        }

        return username;
    }

}
