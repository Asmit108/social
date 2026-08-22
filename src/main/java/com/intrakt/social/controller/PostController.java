package com.intrakt.social.controller;

import com.intrakt.social.models.Post;
import com.intrakt.social.models.User;
import com.intrakt.social.request.PostRequest;
import com.intrakt.social.response.ApiResponse;
import com.intrakt.social.service.PostService;
import com.intrakt.social.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Post Controller", description = "Endpoints for managing posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/posts/user")
    @Operation(summary = "Create a new post", description = "Creates a new post for the authenticated user.")
    public ResponseEntity<Post> createPost(@RequestHeader("Authorization") String jwt, @RequestBody PostRequest post) {
        User reqUser = userService.findUserByJwt(jwt);
        Post createdPost = postService.createNewPost(post,reqUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "Delete a post", description = "Deletes the post identified by the provided ID.")
    public ResponseEntity<ApiResponse> deleteOwnPost(@PathVariable Integer postId, @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        String message = postService.deleteOwnPost(postId,reqUser.getId());
        ApiResponse res = new ApiResponse(message,true);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "Get a post by ID", description = "Retrieves the post identified by the provided ID.")
    public ResponseEntity<Post> findPostById(@PathVariable Integer postId) {
        Post post=postService.findPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("/posts/user/{userId}")
    @Operation(summary = "Get posts by user ID", description = "Retrieves all posts created by the user identified by the provided ID.")
    public ResponseEntity<List<Post>> findUsersPost(@PathVariable Integer userId) {
        List<Post> posts=postService.findPostByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/posts")
    @Operation(summary = "Get all posts", description = "Retrieves all posts.")
    public ResponseEntity<List<Post>> findAllPosts() {
        List<Post> posts=postService.findAllPosts();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @PutMapping("/posts/{postId}")
    @Operation(summary = "Save a post", description = "Saves the post identified by the provided ID.")
    public ResponseEntity<Post> savedPostHandler(@PathVariable Integer postId, @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        Post post=postService.savedPost(postId, reqUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @PutMapping("/posts/like/{postId}")
    @Operation(summary = "Like a post", description = "Likes the post identified by the provided ID.")
    public ResponseEntity<Post> likePostHandler(@PathVariable Integer postId, @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        Post post=postService.likePost(postId, reqUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("/posts/top")
    @Operation(summary = "Get top posts", description = "Retrieves the top posts based on likes.")
    public ResponseEntity<List<Post>> getTopPosts() {
        List<Post> post = postService.getTopPosts();
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @DeleteMapping("/admin/posts/{postId}")
    @Operation(summary = "Delete a post", description = "Deletes the post identified by the provided ID.")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        postService.deletePostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
    }
}