package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.ChatRepository;
import com.intrakt.social.repository.MessageRepository;
import com.intrakt.social.request.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImplementation implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Message createMessage(User user, Integer chatId, MessageRequest messageRequest) {
        Message message = new Message();
        Chat chat = chatService.findChatById(chatId);
        message.setChat(chat);
        message.setUser(user);
        message.setContent(messageRequest.getContent());
        message.setImage(messageRequest.getImage());
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        chat.getMessages().add(savedMessage);
        chatRepository.save(chat);
        return savedMessage;
    }

    @Override
    public List<Message> findChatsMessages(Integer chatId) {
        chatService.findChatById(chatId);
        return messageRepository.findByChatId(chatId);
    }
}
