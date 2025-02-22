package com.intrakt.social.service;

import com.intrakt.social.exceptions.UserException;
import com.intrakt.social.models.User;

import java.util.List;

public interface UserService {
    public User registerUser(User user);
    public User findUserById(Integer userId) throws UserException;
    public User findUserByEmail(String email);
    public User followUser(Integer userId1, Integer userId2) throws UserException;
    public User updateUser(User user, Integer userId) throws UserException;
    public List<User> searchUser(String query ) throws UserException;
    public User findUserByJwt(String jwt) throws UserException;
    public User changeRole(Integer userId, String newRole) throws UserException;
}
