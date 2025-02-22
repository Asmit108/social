package com.intrakt.social.service;

import com.intrakt.social.models.Comment;
import com.intrakt.social.models.Post;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.CommentRepository;
import com.intrakt.social.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Comment createComment(Comment comment, Integer postId, Integer userId) throws Exception {
        User user = userService.findUserById(userId);
        Post post = postService.findPostById(postId);
        Comment newComment=new Comment();
        newComment.setUser(user);
        newComment.setContent(comment.getContent());
        newComment.setCreatedAt(LocalDateTime.now());
        newComment.setUser(user);
        post.getComments().add(newComment);
        newComment.setPost(post);
        postRepository.save(post);
        commentRepository.save(newComment);
        return newComment;
    }

    @Override
    public Comment likeComment(Integer commentId, Integer userId) throws Exception {
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
    public Comment findCommentById(Integer commentId) throws Exception {
        Optional<Comment> opt =  commentRepository.findById(commentId);
        if(opt.isEmpty()){
            throw new Exception("comment not exists");
        }
        return opt.get();
    }
}
