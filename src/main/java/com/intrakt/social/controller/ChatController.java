package com.intrakt.social.controller;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;
import com.intrakt.social.request.CreateChatRequest;
import com.intrakt.social.service.ChatService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/chats")
    public ResponseEntity<Chat> createChat(@RequestBody CreateChatRequest req,
                                          @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        User user2 = userService.findUserById(req.getUserId());
        if(reqUser.equals(user2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create chat with yourself");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(user2.getId(),reqUser.getId()));
    }

    @GetMapping("/chats")
    public ResponseEntity<List<Chat>> findUsersChat(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(chatService.findUsersChat(user.getId()));
    }

    @DeleteMapping("/admin/chats/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable("chatId") Integer chatId) {
        chatService.deleteChatById(chatId);
        return ResponseEntity.status(HttpStatus.OK).body("chat deleted sucessfully");
    }
}
