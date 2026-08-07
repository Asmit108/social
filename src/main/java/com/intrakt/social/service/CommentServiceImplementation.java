package com.intrakt.social.service;

import com.intrakt.social.models.Comment;
import com.intrakt.social.models.Post;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.CommentRepository;
import com.intrakt.social.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentServiceImplementation implements CommentService {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public Comment createComment(String req, Integer postId, Integer userId) {
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);
        Comment newComment=new Comment();
        newComment.setUser(user);
        newComment.setContent(req);
        newComment.setCreatedAt(LocalDateTime.now());
        newComment.setUser(user);
        post.getComments().add(newComment);
        newComment.setPost(post);
        postRepository.save(post);
        commentRepository.save(newComment);
        return newComment;
    }

    @Override
    public Comment likeComment(Integer commentId, Integer userId) {
        Comment comment = findCommentById(commentId);
        User user = userService.findUserById(userId);
        if(!comment.getLiked().contains(user)){
            comment.getLiked().add(user);
        }
        else{
            comment.getLiked().remove(user);
        }
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public Comment findCommentById(Integer commentId) {
        Optional<Comment> opt =  commentRepository.findById(commentId);
        if(opt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        return opt.get();
    }

    @Override
    public void deleteCommentById(Integer commentId) {
        findCommentById(commentId);
        commentRepository.deleteById(commentId);
    }
}
