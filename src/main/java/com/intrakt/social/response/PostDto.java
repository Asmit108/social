package com.intrakt.social.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Integer id;
    private String content;
    private String image;
    private String video;
    private UserDto user;
    private List<Integer> liked = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();
    private List<Integer> savedUser = new ArrayList<>();
}
