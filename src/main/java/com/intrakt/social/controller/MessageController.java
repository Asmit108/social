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

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/messages/chat/{chatId}")
    public Message createMessage(@PathVariable("chatId") Integer chatId, @RequestBody Message req, @RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserByJwt(jwt);
        Message message = messageService.createMessage(reqUser,chatId,req);
        return message;
    }

    @GetMapping("/api/messages/chat/{chatId}")
    public List<Message> findChatMessages(@PathVariable("chatId") Integer chatId, @RequestHeader("Authorization") String jwt) throws Exception {
        User reqUser = userService.findUserByJwt(jwt);
        List<Message> messages = messageService.findChatsMessages(chatId);
        return messages;
    }
}
