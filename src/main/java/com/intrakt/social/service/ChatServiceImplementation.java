package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.ChatRepository;
import com.intrakt.social.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
    public Chat createChat(Integer userId2, String receiverEmail, String receiverFirstName, String receiverLastName, User user) {
         Chat isExist = chatRepository.findChatByUsersId(user.getId(), userId2);
         if(isExist!=null){
             return isExist;
         }
         Chat chat = new Chat();
         chat.setUserId1(user.getId());
         chat.setSenderFirstName(user.getFirstName());
         chat.setSenderLastName(user.getLastName());
         chat.setSenderEmail(user.getEmail());
         chat.setUserId2(userId2);
         chat.setReceiverFirstName(receiverFirstName);
         chat.setReceiverLastName(receiverLastName);
         chat.setReceiverEmail(receiverEmail);
         chat.setChatName(user.getId() + "_" + userId2);
         chat.setTimestamp(LocalDateTime.now());

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
        chatRepository.deleteById(chatId);
    }

    @Override
    public void deleteOwnChat(Integer chatId, Integer userId) {
        Chat chat = findChatById(chatId);
        if (!chat.getUserId1().equals(userId) && !chat.getUserId2().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own chats");
        }
        chatRepository.deleteById(chatId);
    }
}
