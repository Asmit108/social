package com.intrakt.social.mapper;

import com.intrakt.social.models.Post;
import com.intrakt.social.models.User;
import com.intrakt.social.response.CommentDto;
import com.intrakt.social.response.PostDto;
import com.intrakt.social.response.UserDto;

import java.util.List;

public class PostMapper {

    public static PostDto toDto(Post post) {

        PostDto dto = new PostDto();

        dto.setId(post.getId());
        dto.setContent(post.getCaption());
        dto.setImage(post.getImage());
        dto.setVideo(post.getVideo());

        // User -> UserDto
        UserDto userDto = new UserDto();

        userDto.setId(post.getUser().getId());
        userDto.setFirstName(post.getUser().getFirstName());
        userDto.setLastName(post.getUser().getLastName());
        userDto.setEmail(post.getUser().getEmail());
        userDto.setRole(post.getUser().getRole());
        userDto.setGender(post.getUser().getGender());

        dto.setUser(userDto);

        // List<User> -> List<Integer>
        dto.setLiked(
                post.getLiked()
                        .stream()
                        .map(User::getId)
                        .toList()
        );

        // List<Comment> -> List<CommentDto>
        dto.setComments(
                post.getComments()
                        .stream()
                        .map(comment -> {

                            CommentDto commentDto = new CommentDto();

                            commentDto.setId(comment.getId());
                            commentDto.setContent(comment.getContent());
                            commentDto.setUserId(comment.getUser().getId());

                            commentDto.setLiked(
                                    comment.getLiked()
                                            .stream()
                                            .map(User::getId)
                                            .toList()
                            );

                            return commentDto;
                        })
                        .toList()
        );

        // List<User> -> List<Integer>
        dto.setSavedUser(
                post.getSavedUser()
                        .stream()
                        .map(User::getId)
                        .toList()
        );

        return dto;
    }

    public static List<PostDto> toDtoList(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::toDto)
                .toList();
    }
}