package com.intrakt.social.service;

import com.intrakt.social.models.User;

import java.util.List;

public interface UserService {
    User findUserById(Integer userId);
    User findUserByEmail(String email);
    User followUser(Integer userId1, Integer userId2);
    List<User> searchUser(String query );
    User findUserByJwt(String jwt);
    User changeRole(Integer userId, String newRole);
}
