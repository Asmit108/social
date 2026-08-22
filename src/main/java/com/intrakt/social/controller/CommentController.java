package com.intrakt.social.controller;

import com.intrakt.social.models.Comment;
import com.intrakt.social.models.User;
import com.intrakt.social.service.CommentService;
import com.intrakt.social.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Comment Controller", description = "Endpoints for managing comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/comments/post/{postId}")
    @Operation(summary = "Create a new comment", description = "Creates a new comment for the specified post.")
    public ResponseEntity<Comment> createComment(@RequestBody @NotBlank(message = "Comment is required") String req, @RequestHeader("Authorization") String jwt,
                                                 @PathVariable("postId") Integer postId) {
        User user =userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(req,postId,user.getId()));
    }

    @PutMapping("/comments/like/{commentId}")
    @Operation(summary = "Like a comment", description = "Likes the comment identified by the provided ID.")
    public ResponseEntity<Comment> likeComment(@RequestHeader("Authorization") String jwt,
                                 @PathVariable("commentId") Integer commentId) {
        User user =userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.likeComment(commentId,user.getId()));
    }

    @DeleteMapping("/admin/comments/{commentId}")
    @Operation(summary = "Delete a comment", description = "Deletes the comment identified by the provided ID.")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Integer commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully");
    }
}
