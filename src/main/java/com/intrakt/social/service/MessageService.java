package com.intrakt.social.service;

import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;
import com.intrakt.social.request.MessageRequest;

import java.util.List;

public interface MessageService {

    public Message createMessage(User user, Integer chatId, MessageRequest message) ;
    public List<Message> findChatsMessages(Integer chatId) ;
    public void deleteMessageById(Integer messageId);
}
