package com.intrakt.social.service;

import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;
import com.intrakt.social.request.MessageRequest;

import java.util.List;

public interface MessageService {

    Message createMessage(User user, Integer chatId, MessageRequest message) ;
    List<Message> findChatsMessages(Integer chatId) ;
    void deleteMessageById(Integer messageId);
}
