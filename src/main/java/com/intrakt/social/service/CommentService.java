package com.intrakt.social.service;

import com.intrakt.social.models.Comment;

public interface CommentService {
    public Comment createComment(String req, Integer postId, Integer userId);
    public Comment likeComment(Integer commentId,Integer userId);
    public Comment findCommentById(Integer commentId);

}
