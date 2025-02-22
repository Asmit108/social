package com.intrakt.social.service;

import com.intrakt.social.models.Post;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.PostRepository;
import com.intrakt.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PostServiceImplementation implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Override
    public Post createNewPost(Post post, Integer userId) throws Exception {

        User user=userService.findUserById(userId);
        Post newPost=new Post();
        newPost.setCaption(post.getCaption());
        newPost.setImage(post.getImage());
        newPost.setVideo(post.getVideo());
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setUser(user);
        postRepository.save(newPost);
        return newPost;
    }

    @Override
    public String deletePost(Integer postId, Integer userId) throws Exception {
        Post post=findPostById(postId);
        User user=userService.findUserById(userId);
        if (post.getUser().getId() != user.getId()) {
            throw new Exception("you can't delete another user's post");
        } else {
            postRepository.delete(post);
            return "post deleted successfully";
        }
    }

    @Override
    public List<Post> findPostByUserId(Integer userId) throws Exception {
        List<Post> posts=postRepository.findPostByUserId(userId);
        return posts;
    }

    @Override
    public Post findPostById(Integer postId) throws Exception {
        Optional<Post> opt=postRepository.findById(postId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("no user found with postid "+postId);
    }

    @Override
    public List<Post> findAllPosts() {
        List<Post> posts=postRepository.findAll();
        return posts;
    }

    @Override
    public Post savedPost(Integer postId, Integer userId) throws Exception {
        Post post=findPostById(postId);
        User user=userService.findUserById(userId);
        if(user.getSavedPost().contains(post)){
            user.getSavedPost().remove(post);
        } else{
            user.getSavedPost().add(post);
        }
        userRepository.save(user);
        return post;
    }

    @Override
    public Post likePost(Integer postId, Integer userId) throws Exception {
        Post post=findPostById(postId);
        User user=userService.findUserById(userId);
        if (!post.getLiked().contains(user)) {
            post.getLiked().add(user);
        } else {
            post.getLiked().remove(user);
        }
        postRepository.save(post);
        return post;
    }
}
