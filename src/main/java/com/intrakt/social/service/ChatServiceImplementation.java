package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImplementation implements ChatService {


    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat createChat(User reqUser, User user2) {
         Chat isExist = chatRepository.findChatByUsersId(user2,reqUser);
         if(isExist!=null){
             return isExist;
         }

         Chat chat = new Chat();
         chat.getUsers().add(user2);
         chat.getUsers().add(reqUser);

         return chatRepository.save(chat);
    }

    @Override
    public Chat findChatById(Integer chatId) throws Exception {
        Optional<Chat> opt=chatRepository.findById(chatId);
        if(opt.isEmpty()){
            throw new Exception("chat not found with id-" + chatId);
        }

        return opt.get();
    }

    @Override
    public List<Chat> findUsersChat(Integer userId) {
        List<Chat> chats = chatRepository.findByUsersId(userId);
        return chats;
    }
}
