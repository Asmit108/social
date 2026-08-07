package com.intrakt.social.service;

import com.intrakt.social.models.Post;
import com.intrakt.social.request.PostRequest;

import java.util.List;

public interface PostService {

    public Post createNewPost(PostRequest post, Integer userId);
    public String deletePost(Integer postId, Integer userId);
    public List<Post> findPostByUserId(Integer userId);
    public Post findPostById(Integer postId);
    public List<Post> findAllPosts();
    public Post savedPost(Integer postId, Integer userId);
    public Post likePost(Integer postId,Integer userId);
    public void deletePostById(Integer postId);
}
