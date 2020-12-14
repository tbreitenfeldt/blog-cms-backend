package com.timothybreitenfeldt.blog;

import com.timothybreitenfeldt.blog.model.Post;
import com.timothybreitenfeldt.blog.model.User;

public class BlogModelFactory {

    public static Post createPost(String title, String content, Long userId) {
        Post post = new Post();
        User user = new User();

        post.setTitle(title);
        post.setContent(content);
        user.setId(userId);
        post.setUser(user);
        return post;
    }

}