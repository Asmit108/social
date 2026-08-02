package com.intrakt.social.controller;

import com.intrakt.social.models.Comment;
import com.intrakt.social.models.User;
import com.intrakt.social.service.CommentService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Comment createComment(@RequestBody Comment comment, @RequestHeader("Authorization") String jwt,
                                 @PathVariable("postId") Integer postId) throws Exception {
        User user =userService.findUserByJwt(jwt);
        return commentService.createComment(comment,postId,user.getId());
    }

    @PutMapping("/api/comments/like/{commentId}")
    public Comment likeComment(@RequestHeader("Authorization") String jwt,
                                 @PathVariable("commentId") Integer commentId) throws Exception {
        User user =userService.findUserByJwt(jwt);
        return commentService.likeComment(commentId,user.getId());
    }
}
