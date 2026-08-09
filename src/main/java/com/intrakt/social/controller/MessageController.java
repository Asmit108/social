package com.intrakt.social.controller;

import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;
import com.intrakt.social.request.MessageRequest;
import com.intrakt.social.service.MessageService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/messages/chat/{chatId}")
    public ResponseEntity<Message> createMessage(@PathVariable("chatId") Integer chatId, @RequestBody MessageRequest message, @RequestHeader("Authorization") String jwt) {
        User reqUser = userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(reqUser.getId(),chatId,message));
    }

    @GetMapping("/messages/chat/{chatId}")
    public ResponseEntity<List<Message>> findChatMessages(@PathVariable("chatId") Integer chatId) {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.findChatsMessages(chatId));
    }

    @DeleteMapping("/admin/messages/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable("messageId") Integer messageId) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body("Message deleted successfully");
    }
}
