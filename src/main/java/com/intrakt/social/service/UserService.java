package com.intrakt.social.service;

import com.intrakt.social.models.User;
import com.intrakt.social.request.UserRequest;

import java.util.List;

public interface UserService {
    public User registerUser(User user);
    public User findUserById(Integer userId);
    public User findUserByEmail(String email);
    public User followUser(Integer userId1, Integer userId2);
    public User updateUser(UserRequest user, Integer userId);
    public List<User> searchUser(String query );
    public User findUserByJwt(String jwt);
    public User changeRole(Integer userId, String newRole);
}
