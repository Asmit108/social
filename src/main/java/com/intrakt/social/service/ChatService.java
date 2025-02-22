package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;

import java.util.List;

public interface ChatService {

    public Chat createChat(User reqUser,User user2 );

    public Chat findChatById(Integer chatId) throws Exception;

    public List<Chat> findUsersChat(Integer userId);
}
