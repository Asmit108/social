package com.intrakt.social.service;

import com.intrakt.social.config.JwtProvider;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserById(Integer userId) {

        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User followUser(Integer reqUserId, Integer userId2) {
        User reqUser=findUserById(reqUserId);
        User user2=findUserById(userId2);
        if (!user2.getFollowers().contains(reqUser)) {
            user2.getFollowers().add(reqUser);
            reqUser.getFollowings().add(user2);
        } else {
            user2.getFollowers().remove(reqUser);
            reqUser.getFollowings().remove(user2);
        }
        userRepository.save(reqUser);
        userRepository.save(user2);
        return reqUser;
    }

    @Override
    public User updateUser(UserRequest user, Integer userId) {
        Optional<User> user1=userRepository.findById(userId);
        if(user1.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
        User oldUser=user1.get();
        if(user.getFirstName()!=null){
            oldUser.setFirstName(user.getFirstName());
        }
        if(user.getLastName()!=null){
            oldUser.setLastName(user.getLastName());
        }
        if(user.getEmail()!=null){
            oldUser.setFirstName(user.getFirstName());
        }
        if(user.getGender()!=null){
            oldUser.setGender(user.getGender());
        }
        return userRepository.save(oldUser);
    }

    @Override
    public List<User> searchUser(String query) {
        return userRepository.searchUser(query);
    }

    @Override
    public User findUserByJwt(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return userRepository.findByEmail(email);
    }

    @Override
    public User changeRole(Integer userId, String newRole) {
        User user = findUserById(userId);
        user.setRole(User.Role.valueOf(newRole));
        user = userRepository.save(user);
        return user;
    }

}
