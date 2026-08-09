package com.intrakt.social.service;

import com.intrakt.social.models.Post;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.PostRepository;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.request.PostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostServiceImplementation implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PostServiceImplementation(PostRepository postRepository, UserRepository userRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public Post createNewPost(PostRequest post, Integer userId) {

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
    public String deleteOwnPost(Integer postId, Integer userId) {
        Post post=findPostById(postId);
        User user=userService.findUserById(userId);
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this post");
        } else {
            postRepository.delete(post);
            return "post deleted successfully";
        }
    }

    @Override
    public List<Post> findPostByUserId(Integer userId) {
        return postRepository.findPostByUserId(userId);
    }

    @Override
    public Post findPostById(Integer postId) {
        Optional<Post> opt=postRepository.findById(postId);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with id: " + postId);
    }

    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post savedPost(Integer postId, Integer userId) {
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
    public Post likePost(Integer postId, Integer userId) {
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

    @Override
    public void deletePostById(Integer postId) {
        findPostById(postId);
        postRepository.deleteById(postId);
    }

    @Override
    public List<Post> getTopPosts() {
        return postRepository.getTopPosts();
    }
}
