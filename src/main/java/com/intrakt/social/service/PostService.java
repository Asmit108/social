package com.intrakt.social.service;

import com.intrakt.social.models.Post;
import com.intrakt.social.request.PostRequest;

import java.util.List;

public interface PostService {

    Post createNewPost(PostRequest post, Integer userId);
    String deletePost(Integer postId, Integer userId);
    List<Post> findPostByUserId(Integer userId);
    Post findPostById(Integer postId);
    List<Post> findAllPosts();
    Post savedPost(Integer postId, Integer userId);
    Post likePost(Integer postId,Integer userId);
}
