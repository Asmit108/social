package com.intrakt.social.controller;

import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.request.UserRequest;
import com.intrakt.social.service.CustomeUserDetailsService;
import com.intrakt.social.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Tag(name = "User Controller", description = "Endpoints for managing users")
public class UserController {

    private final CustomeUserDetailsService customeUserDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, CustomeUserDetailsService customeUserDetailsService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.customeUserDetailsService = customeUserDetailsService;
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieves the user identified by the provided ID.")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) {
        User user=userService.findUserById(userId);
        userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/users")
    @Operation(summary = "Update user", description = "Updates the details of an existing user.")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String jwt,@RequestBody UserRequest user) {
        User reqUser=userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(customeUserDetailsService.updateUser(user,reqUser.getId()));
    }

    @DeleteMapping("/users")
    @Operation(summary = "Delete user", description = "Deletes the user identified by the provided JWT.")
    public ResponseEntity<String> removeAccount( @RequestHeader("Authorization") String jwt) {
        User reqUser=userService.findUserByJwt(jwt);
        userRepository.deleteById(reqUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

    @PutMapping("/users/follow/{userId2}")
    @Operation(summary = "Follow user", description = "Allows the authenticated user to follow another user.")
    public ResponseEntity<User> followUserHandler(@RequestHeader("Authorization") String jwt,@PathVariable Integer userId2) {
        User reqUser=userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(userService.followUser(reqUser.getId(),userId2));
    }

    @GetMapping("/users/search")
    @Operation(summary = "Search users", description = "Searches for users based on the provided query.")
    public ResponseEntity<List<User>> searchUser(@RequestParam("query") String query) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.searchUser(query));
    }

    @GetMapping("/users/profile")
    @Operation(summary = "Get user profile", description = "Retrieves the profile of the authenticated user.")
    public ResponseEntity<User> getOwnProfile(@RequestHeader("Authorization") String jwt) {
        User user=userService.findUserByJwt(jwt);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/admin/users/{userId}")
    @Operation(summary = "Delete user", description = "Deletes the user identified by the provided ID.")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) {
        Optional<User> user1=userRepository.findById(userId);
        userService.findUserByJwt(jwt);
        if(user1.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

    @PutMapping("/admin/users/{userId}")
    @Operation(summary = "Update user", description = "Updates the details of an existing user identified by the provided ID.")
    public ResponseEntity<User> changeRole(@PathVariable Integer userId, @RequestParam("newRole") String newRole) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeRole(userId, newRole));
    }
}
