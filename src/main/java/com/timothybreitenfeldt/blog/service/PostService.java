package com.timothybreitenfeldt.blog.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.timothybreitenfeldt.blog.dto.PostHeaderPagerDto;
import com.timothybreitenfeldt.blog.dto.PostHeaderResponseDto;
import com.timothybreitenfeldt.blog.dto.PostRequestDto;
import com.timothybreitenfeldt.blog.dto.PostResponseDto;
import com.timothybreitenfeldt.blog.exception.PostNotFoundException;
import com.timothybreitenfeldt.blog.exception.UserNotAuthenticatedException;
import com.timothybreitenfeldt.blog.model.Post;
import com.timothybreitenfeldt.blog.model.User;
import com.timothybreitenfeldt.blog.repository.PostRepository;
import com.timothybreitenfeldt.blog.security.UserDetailsImpl;

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

    public PostHeaderPagerDto getAllPostHeaders(int pageNumber, int pageSize) {
        List<Post> posts = this.postRepository.findAll();
        List<PostHeaderResponseDto> postHeaderResponseDtos = posts.stream()
                .map(this::mapFromPostModelToPostHeaderResponseDto).collect(Collectors.toList());
        PostHeaderPagerDto postHeaderPagerDto = this.paginate(postHeaderResponseDtos, pageNumber, pageSize);
        return postHeaderPagerDto;
    }

    public PostHeaderPagerDto getPostHeadersForAuthor(int pageNumber, int pageSize) {
        Long userId = this.getUserIdFromSecurityContext();
        List<Post> posts = this.postRepository.findAllByUserId(userId);
        List<PostHeaderResponseDto> postHeaderResponseDtos = posts.stream()
                .map(this::mapFromPostModelToPostHeaderResponseDto).collect(Collectors.toList());
        PostHeaderPagerDto postHeaderPagerDto = this.paginate(postHeaderResponseDtos, pageNumber, pageSize);
        return postHeaderPagerDto;
    }

    public PostHeaderPagerDto searchForPostsByTitle(String title, int pageNumber, int pageSize) {
        List<Post> posts = this.postRepository.findByTitleContaining(title);
        List<PostHeaderResponseDto> postHeaderResponseDtos = posts.stream()
                .map(this::mapFromPostModelToPostHeaderResponseDto).collect(Collectors.toList());
        PostHeaderPagerDto postHeaderPagerDto = this.paginate(postHeaderResponseDtos, pageNumber, pageSize);
        return postHeaderPagerDto;
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

        boolean includeUserId = false;
        Post post = this.mapFromPostRequestDtoToPostModel(id, postRequestDto, includeUserId);
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

        Long userId = this.getUserIdFromSecurityContext();
        this.postRepository.deleteByIdAndUserId(id, userId);
    }

    private PostHeaderPagerDto paginate(List<PostHeaderResponseDto> content, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int total = content.size();
        Page<PostHeaderResponseDto> pagedPosts = new PageImpl<>(content, pageable, total);
        PostHeaderPagerDto postHeaderPagerDto = new PostHeaderPagerDto(pagedPosts.getNumber(),
                pagedPosts.getTotalElements(), pagedPosts.getTotalPages(), pagedPosts.getContent());
        return postHeaderPagerDto;
    }

    private Post mapFromPostRequestDtoToPostModel(PostRequestDto postRequestDto) {
        Long id = null;
        boolean includeUserId = true;
        return this.mapFromPostRequestDtoToPostModel(id, postRequestDto, includeUserId);
    }

    private Post mapFromPostRequestDtoToPostModel(Long id, PostRequestDto postRequestDto) {
        boolean includeUserId = true;
        return this.mapFromPostRequestDtoToPostModel(id, postRequestDto, includeUserId);
    }

    private Post mapFromPostRequestDtoToPostModel(Long id, PostRequestDto postRequestDto, boolean includeUserId) {
        Post post = new Post();
        User user = null;

        if (id != null) {
            post.setId(id);
        }

        if (includeUserId) {
            user = new User();
            Long userId = this.getUserIdFromSecurityContext();
            user.setId(userId);
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
        postResponseDto.setUsername(this.getUsernameFromSecurityContext());
        postResponseDto.setCreatedOn(post.getCreatedOn());
        postResponseDto.setUpdatedOn(post.getUpdatedOn());
        return postResponseDto;
    }

    public Long getUserIdFromSecurityContext() {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (userDetailsImpl == null) {
            throw new UserNotAuthenticatedException("Unable to retrieve user ID.");
        }

        return userDetailsImpl.getUserId();
    }

    public String getUsernameFromSecurityContext() {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        if (userDetailsImpl == null) {
            throw new UserNotAuthenticatedException("Unable to retrieve username.");
        }

        return userDetailsImpl.getUsername();
    }

}
