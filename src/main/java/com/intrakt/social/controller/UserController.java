package com.intrakt.social.controller;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.request.UserRequest;
import com.intrakt.social.response.UserDto;
import com.intrakt.social.service.ChatService;
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
    private final ChatService chatService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, CustomeUserDetailsService customeUserDetailsService, ChatService chatService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.customeUserDetailsService = customeUserDetailsService;
        this.chatService = chatService;
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    public ResponseEntity<List<UserDto>> getUsers(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getGender()
                ))
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieves the user identified by the provided ID.")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        User user=userService.findUserById(userId);
        userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/users")
    @Operation(summary = "Update user", description = "Updates the details of an existing user.")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role, @RequestBody UserRequest user) {
        User reqUser=userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(customeUserDetailsService.updateUser(user,reqUser.getId()));
    }

    @DeleteMapping("/users")
    @Operation(summary = "Delete user", description = "Deletes the user identified by the provided JWT.")
    public ResponseEntity<String> removeAccount(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        User reqUser=userService.findUserByJwt(jwt);
        List<Chat> chats = chatService.findUsersChat(reqUser.getId());
        for (Chat chat : chats) {
            chatService.deleteChatById(chat.getId());
        }
        userRepository.deleteById(reqUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

    @PutMapping("/users/follow/{userId2}")
    @Operation(summary = "Follow user", description = "Allows the authenticated user to follow another user.")
    public ResponseEntity<User> followUserHandler(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role, @PathVariable Integer userId2) {
        User reqUser=userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(userService.followUser(reqUser.getId(),userId2));
    }

    @GetMapping("/users/search")
    @Operation(summary = "Search users", description = "Searches for users based on the provided query.")
    public ResponseEntity<List<User>> searchUser(@RequestParam("query") String query, @RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.searchUser(query));
    }

    @GetMapping("/users/profile")
    @Operation(summary = "Get user profile", description = "Retrieves the profile of the authenticated user.")
    public ResponseEntity<User> getOwnProfile(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        User user=userService.findUserByJwt(jwt);
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/admin/users/{userId}")
    @Operation(summary = "Delete user", description = "Deletes the user identified by the provided ID.")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        Optional<User> user1=userRepository.findById(userId);
        userService.findUserByJwt(jwt);
        if(user1.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
        List<Chat> chats = chatService.findUsersChat(userId);
        for (Chat chat : chats) {
            chatService.deleteChatById(chat.getId());
        }
        userRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

    @PutMapping("/admin/users/{userId}")
    @Operation(summary = "Update user", description = "Updates the details of an existing user identified by the provided ID.")
    public ResponseEntity<User> changeRole(@PathVariable Integer userId, @RequestParam("newRole") String newRole, @RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.changeRole(userId, newRole));
    }
}
