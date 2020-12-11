package com.timothybreitenfeldt.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timothybreitenfeldt.blog.dto.PostRequestDto;
import com.timothybreitenfeldt.blog.dto.PostResponseDto;
import com.timothybreitenfeldt.blog.exception.PostNotFoundException;
import com.timothybreitenfeldt.blog.model.Post;
import com.timothybreitenfeldt.blog.model.User;
import com.timothybreitenfeldt.blog.repository.PostRepository;
import com.timothybreitenfeldt.blog.util.JWTUtil;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Post postModel = this.mapFromPostRequestDtoToPostModel(postRequestDto);
        Post result = this.postRepository.save(postModel);
        return this.mapFromPostModelToPostResponseDto(result);
    }

    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = this.postRepository.findAll();
        return posts.stream().map(this::mapFromPostModelToPostResponseDto).collect(Collectors.toList());
    }

    public List<PostResponseDto> getPostsForAuthor() {
        String username = this.jwtUtil.extractSubject();
        List<Post> posts = this.postRepository.findAllByUsername(username);
        return posts.stream().map(this::mapFromPostModelToPostResponseDto).collect(Collectors.toList());
    }

    public PostResponseDto getPost(Long id) {
        Post post = this.postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " cannot be found"));
        return this.mapFromPostModelToPostResponseDto(post);
    }

    @Transactional
    public void updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = this.mapFromPostRequestDtoToPostModel(id, postRequestDto);
        this.postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        this.postRepository.deleteById(id);
    }

    private Post mapFromPostRequestDtoToPostModel(PostRequestDto postRequestDto) {
        return this.mapFromPostRequestDtoToPostModel(null, postRequestDto);
    }

    private Post mapFromPostRequestDtoToPostModel(Long id, PostRequestDto postRequestDto) {
        Post post = new Post();
        User user = new User();
        String username = this.jwtUtil.extractSubject();

        if (id != null) {
            post.setId(id);
        }

        user.setUsername(username);
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setUser(user);
        return post;
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

}
