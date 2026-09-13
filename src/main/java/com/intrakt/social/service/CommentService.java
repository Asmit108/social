package com.intrakt.social.service;

import com.intrakt.social.models.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(String req, Integer postId, Integer userId);
    Comment likeComment(Integer commentId,Integer userId);
    Comment findCommentById(Integer commentId);
    List<Comment> findCommentsByPostId(Integer postId);
    void deleteCommentById(Integer commentId);
}
