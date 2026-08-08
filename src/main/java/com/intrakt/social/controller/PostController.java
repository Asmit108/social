package com.intrakt.social.controller;

import com.intrakt.social.models.Post;
import com.intrakt.social.models.User;
import com.intrakt.social.request.PostRequest;
import com.intrakt.social.response.ApiResponse;
import com.intrakt.social.service.PostService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/api/posts/user")
    public ResponseEntity<Post> createPost(@RequestHeader("Authorization") String jwt, @RequestBody PostRequest post) {
        User reqUser = userService.findUserByJwt(jwt);
        Post createdPost= postService.createNewPost(post,reqUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId, @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        String message=postService.deletePost(postId,reqUser.getId());
        ApiResponse res=new ApiResponse(message,true);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<Post> findPostById(@PathVariable Integer postId) {
        Post post=postService.findPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("/api/posts/user/{userId}")
    public ResponseEntity<List<Post>> findUsersPost(@PathVariable Integer userId) {

        List<Post> posts=postService.findPostByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<Post>> findAllPosts() {
        List<Post> posts=postService.findAllPosts();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<Post> savedPostHandler(@PathVariable Integer postId, @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        Post post=postService.savedPost(postId, reqUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PutMapping("/api/posts/like/{postId}")
    public ResponseEntity<Post> likePostHandler(@PathVariable Integer postId, @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        Post post=postService.likePost(postId, reqUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("/api/posts/most-liked")
    public ResponseEntity<List<Post>> getMostLikedPost() {
        List<Post> post = postService.getTopPosts();
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @DeleteMapping("/api/admin/posts/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        postService.deletePostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
    }
}