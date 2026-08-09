package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;

import java.util.List;

public interface ChatService {

    Chat createChat(Integer userId1,Integer userId2);

    Chat findChatById(Integer chatId);

    List<Chat> findUsersChat(Integer userId);

    void deleteChatById(Integer chatId);
}
