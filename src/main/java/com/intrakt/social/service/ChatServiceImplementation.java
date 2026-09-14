package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.Message;
import com.intrakt.social.repository.ChatRepository;
import com.intrakt.social.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImplementation implements ChatService {

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatServiceImplementation(ChatRepository chatRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Chat createChat(Integer userId2, Integer userId1) {
         Chat isExist = chatRepository.findChatByUsersId(userId1, userId2);
         if(isExist!=null){
             return isExist;
         }
         Chat chat = new Chat();
         chat.setUserId1(userId1);
         chat.setUserId2(userId2);

         return chatRepository.save(chat);
    }

    @Override
    public Chat findChatById(Integer chatId) {
        Optional<Chat> opt=chatRepository.findById(chatId);
        if(opt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
        }

        return opt.get();
    }

    @Override
    public List<Chat> findUsersChat(Integer userId) {
        return chatRepository.findByUsersId(userId);
    }

    @Override
    public List<Chat> findAllChats() {
        return chatRepository.findAll();
    }

    @Override
    public void deleteChatById(Integer chatId) {
        Chat chat = findChatById(chatId);
        List<Message> messages = messageRepository.findByChatId(chatId);
        for (Message message : messages) {
            chat.getMessageIds().remove(message.getId());
            chatRepository.save(chat);
            messageRepository.deleteById(message.getId());
        }
        chatRepository.deleteById(chatId);
    }

    @Override
    public void deleteOwnChat(Integer chatId, Integer userId) {
        Chat chat = findChatById(chatId);
        if (!chat.getUserId1().equals(userId) && !chat.getUserId2().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own chats");
        }
        List<Message> messages = messageRepository.findByChatId(chatId);
        for (Message message : messages) {
            chat.getMessageIds().remove(message.getId());
            chatRepository.save(chat);
            messageRepository.deleteById(message.getId());
        }
        chatRepository.deleteById(chatId);
    }
}
