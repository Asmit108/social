package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;

import java.util.List;

public interface ChatService {

    Chat createChat(Integer userId1, String receiverEmail, String receiverFirstName, String receiverLastName, User user);

    Chat findChatById(Integer chatId);

    List<Chat> findUsersChat(Integer userId);

    List<Chat> findAllChats();

    void deleteChatById(Integer chatId);

    void deleteOwnChat(Integer chatId, Integer userId);
}
