package com.intrakt.social.controller;

import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.request.UserRequest;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) {
        User user=userService.findUserById(userId);
        userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/api/users")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String jwt,@RequestBody UserRequest user) {
        User reqUser=userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user,reqUser.getId()));
    }

    @DeleteMapping("/api/users")
    public ResponseEntity<String> removeAccount( @RequestHeader("Authorization") String jwt) {
        User reqUser=userService.findUserByJwt(jwt);
        userRepository.deleteById(reqUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

    @PutMapping("/api/users/follow/{userId2}")
    public ResponseEntity<User> followUserHandler(@RequestHeader("Authorization") String jwt,@PathVariable Integer userId2) {
         User reqUser=userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(userService.followUser(reqUser.getId(),userId2));
    }

    @GetMapping("/api/users/search")
    public ResponseEntity<List<User>> searchUser(@RequestParam("query") String query) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.searchUser(query));
    }

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserFromToken(@RequestHeader("Authorization") String jwt) {
        User user=userService.findUserByJwt(jwt);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/api/admin/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) {
        Optional<User> user1=userRepository.findById(userId);
        userService.findUserByJwt(jwt);
        if(user1.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

    @PutMapping("/api/admin/users/{userId}")
    public ResponseEntity<User> changeRole(@RequestHeader("Authorization") String jwt,@PathVariable Integer userId, @RequestParam("newRole") String newRole) {
        userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeRole(userId, newRole));
    }
}
