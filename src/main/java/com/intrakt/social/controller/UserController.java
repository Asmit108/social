package com.intrakt.social.controller;

import com.intrakt.social.exceptions.UserException;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/api/users/{userId}")
    public User getUserById(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) throws UserException {
        User user=userService.findUserById(userId);
        userService.findUserByJwt(jwt);
        return user;
    }

    @PutMapping("/api/users")
    public User updateUser(@RequestHeader("Authorization") String jwt,@RequestBody User user) throws UserException {
        User reqUser=userService.findUserByJwt(jwt);
        return userService.updateUser(user,reqUser.getId());
    }

    @DeleteMapping("/api/users")
    public String removeAccount( @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser=userService.findUserByJwt(jwt);
        userRepository.deleteById(reqUser.getId());
        return "user deleted successfully";
    }

    @PutMapping("/api/users/follow/{userId2}")
    public User followUserHandler(@RequestHeader("Authorization") String jwt,@PathVariable Integer userId2) throws UserException{
         User reqUser=userService.findUserByJwt(jwt);
        return userService.followUser(reqUser.getId(),userId2);
    }

    @GetMapping("/api/users/search")
    public List<User> searchUser(@RequestParam("query") String query) throws UserException{
        return userService.searchUser(query);
    }

    @GetMapping("/api/users/profile")
    public User getUserFromToken(@RequestHeader("Authorization") String jwt) throws UserException{
        User user=userService.findUserByJwt(jwt);
        user.setPassword(null);
        return user;
    }


    @DeleteMapping("/api/admin/users/{userId}")
    public String deleteUser(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) throws UserException {
        Optional<User> user1=userRepository.findById(userId);
        userService.findUserByJwt(jwt);
        if(user1.isEmpty()) {
            throw new UserException("user not found with id" + userId);
        }
        userRepository.deleteById(userId);
        return "user deleted successfully";
    }

    @PutMapping("/api/admin/users/{userId}")
    public User changeRole(@RequestHeader("Authorization") String jwt,@PathVariable Integer userId, @RequestParam("newRole") String newRole) throws UserException{
        userService.findUserByJwt(jwt);
        return userService.changeRole(userId, newRole);
    }
}
