package com.intrakt.social.controller;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;
import com.intrakt.social.request.CreateChatRequest;
import com.intrakt.social.service.ChatService;
import com.intrakt.social.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Chat Controller", description = "Endpoints for managing chats between users")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/chats")
    @Operation(summary = "Create a new chat", description = "Creates a new chat between two users.")
    public ResponseEntity<Chat> createChat(@Valid @RequestBody CreateChatRequest req, @RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        User reqUser = userService.findUserByJwt(jwt);
        User user2 = userService.findUserById(req.getUserId());
        if(reqUser.equals(user2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create chat with yourself");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(user2.getId(),reqUser.getId()));
    }

    @GetMapping("/chats")
    @Operation(summary = "Get all chats for the authenticated user", description = "Retrieves all chats for the user identified by the provided JWT.")
    public ResponseEntity<List<Chat>> findUsersChat(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        User user = userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(chatService.findUsersChat(user.getId()));
    }

    @GetMapping("/admin/chats")
    @Operation(summary = "Get all chats", description = "Retrieves all chats")
    public ResponseEntity<List<Chat>> findAllChats(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.findAllChats());
    }

    @DeleteMapping("/admin/chats/{chatId}")
    @Operation(summary = "Delete a chat", description = "Deletes a chat identified by the provided ID.")
    public ResponseEntity<String> deleteChat(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role, @PathVariable("chatId") Integer chatId) {
        chatService.deleteChatById(chatId);
        return ResponseEntity.status(HttpStatus.OK).body("chat deleted sucessfully");
    }

    @DeleteMapping("/chats/{chatId}")
    @Operation(summary = "Delete a chat", description = "Deletes a chat identified by the provided ID.")
    public ResponseEntity<String> deleteOwnChat(@RequestHeader("Authorization") String jwt, @RequestHeader("Role") String role, @PathVariable("chatId") Integer chatId) {
        User user = userService.findUserByJwt(jwt);
        chatService.deleteOwnChat(chatId, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("chat deleted sucessfully");
    }
}
