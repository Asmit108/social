package com.intrakt.social.request;

import com.intrakt.social.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private User.Role role;
    private User.Gender gender;
}
