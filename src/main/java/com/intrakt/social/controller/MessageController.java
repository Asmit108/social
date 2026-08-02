package com.intrakt.social.controller;

import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;
import com.intrakt.social.service.MessageService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/api/messages/chat/{chatId}")
    public Message createMessage(@PathVariable("chatId") Integer chatId, @RequestBody Message req, @RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserByJwt(jwt);
        return messageService.createMessage(reqUser,chatId,req);
    }

    @GetMapping("/api/messages/chat/{chatId}")
    public List<Message> findChatMessages(@PathVariable("chatId") Integer chatId, @RequestHeader("Authorization") String jwt) throws Exception {
        userService.findUserByJwt(jwt);
        return messageService.findChatsMessages(chatId);
    }
}
