package com.intrakt.social.controller;

import com.intrakt.social.models.Comment;
import com.intrakt.social.models.User;
import com.intrakt.social.service.CommentService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/api/comments/post/{postId}")
    public ResponseEntity<Comment> createComment(@RequestBody String req, @RequestHeader("Authorization") String jwt,
                                                @PathVariable("postId") Integer postId) {
        User user =userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(req,postId,user.getId()));
    }

    @PutMapping("/api/comments/like/{commentId}")
    public ResponseEntity<Comment> likeComment(@RequestHeader("Authorization") String jwt,
                                 @PathVariable("commentId") Integer commentId) {
        User user =userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.likeComment(commentId,user.getId()));
    }
}
