package com.timothybreitenfeldt.blog.unit.service;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.timothybreitenfeldt.blog.BlogModelFactory;
import com.timothybreitenfeldt.blog.dto.PostRequestDto;
import com.timothybreitenfeldt.blog.dto.PostResponseDto;
import com.timothybreitenfeldt.blog.model.Post;
import com.timothybreitenfeldt.blog.repository.PostRepository;
import com.timothybreitenfeldt.blog.service.PostService;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void testCreateSuccessfulPost() {
        String title = "test title";
        String content = "test content";
        Long userId = 1l;
        PostRequestDto postRequestDto = new PostRequestDto(title, content);
        Post post = BlogModelFactory.createPost(title, content, userId);
        Mockito.when(this.postRepository.save(post)).thenReturn(post);
        Mockito.when(this.postService.getUserIdFromSecurityContext()).thenReturn(userId);
        PostResponseDto result = this.postService.createPost(postRequestDto);
        String[] expected = new String[] { title, content };
        String[] actual = new String[] { result.getTitle(), result.getContent() };
        Assertions.assertArrayEquals(expected, actual);
        Assertions.assertEquals(userId, result.getId());
    }

    @Test
    void testGetAllPostHeaders() {
        fail("Not yet implemented");
    }

    @Test
    void testGetPostHeadersForAuthor() {
        fail("Not yet implemented");
    }

    @Test
    void testGetPost() {
        fail("Not yet implemented");
    }

    @Test
    void testUpdateAnyPost() {
        fail("Not yet implemented");
    }

    @Test
    void testUpdatePostForUser() {
        fail("Not yet implemented");
    }

    @Test
    void testDeleteAnyPost() {
        fail("Not yet implemented");
    }

    @Test
    void testDeletePostForUser() {
        fail("Not yet implemented");
    }

}
