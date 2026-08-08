package com.intrakt.social.service;

import com.intrakt.social.models.Comment;

public interface CommentService {
    Comment createComment(String req, Integer postId, Integer userId);
    Comment likeComment(Integer commentId,Integer userId);
    Comment findCommentById(Integer commentId);
    void deleteCommentById(Integer commentId);
}
