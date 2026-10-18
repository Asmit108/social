package com.intrakt.social.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChatResponse {
    private Integer id;
    private String chatName;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
}
