package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;

import java.util.List;

public interface MessageService {

    public Message createMessage(User user, Integer chatId, Message req) throws Exception;
    public List<Message> findChatsMessages(Integer chatId) throws Exception;
}
