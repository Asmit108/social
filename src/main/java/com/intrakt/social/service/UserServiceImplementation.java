package com.intrakt.social.service;

import com.intrakt.social.config.JwtProvider;
import com.intrakt.social.exceptions.UserException;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        User newUser=new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(user.getPassword());
        User savedUser=userRepository.save(newUser);
        return savedUser;
    }

    @Override
    public User findUserById(Integer userId) throws UserException {

        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()) {
            return user.get();
        }
        throw new UserException("no user found with userid"+userId);
    }

    @Override
    public User findUserByEmail(String email) {
        User user=userRepository.findByEmail(email);
        return user;
    }

    @Override
    public User followUser(Integer reqUserId, Integer userId2) throws UserException {
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
    public User updateUser(User user,Integer userId) throws UserException {
        Optional<User> user1=userRepository.findById(userId);
        if(user1.isEmpty()){
            throw new UserException("user not found with id"+userId);
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
            oldUser.setGender(user.getGender());;
        }
        User updatedUser=userRepository.save(oldUser);

        return updatedUser;
    }

    @Override
    public List<User> searchUser(String query) throws UserException {
        List<User> users=userRepository.searchUser(query);
        return users;
    }

    @Override
    @Cacheable(value = "userProfiles", key = "#jwt")
    public User findUserByJwt(String jwt) throws UserException {
        System.out.println("Fetching user from database...");
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public User changeRole(Integer userId, String newRole) throws UserException {
        User user = findUserById(userId);
        user.setRole(newRole);
        user = userRepository.save(user);
        return user;
    }

}
