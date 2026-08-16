package com.intrakt.social.service;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.Message;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.ChatRepository;
import com.intrakt.social.repository.MessageRepository;
import com.intrakt.social.request.MessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessageServiceImplementation implements MessageService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public MessageServiceImplementation(SimpMessagingTemplate simpMessagingTemplate, MessageRepository messageRepository, ChatRepository chatRepository) {
        this.messagingTemplate = simpMessagingTemplate;
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public Message createMessage(Integer userId, Integer chatId, MessageRequest messageRequest) {
        Message message = new Message();
        Optional<Chat> opt=chatRepository.findById(chatId);
        if(opt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
        }
        Chat chat = opt.get();
        if(!Objects.equals(userId, chat.getUserId1()) && !Objects.equals(chat.getUserId2(), userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not allowed to send message");
        }
        message.setChatId(chatId);
        message.setSenderId(userId);
        if(Objects.equals(chat.getUserId1(), userId)) {
            message.setReceiverId(chat.getUserId2());
        } else if(Objects.equals(chat.getUserId2(), userId)) {
            message.setReceiverId(chat.getUserId1());
        }
        message.setContent(messageRequest.getContent());
        message.setImage(messageRequest.getImage());
        message.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);
        chat.getMessageIds().add(savedMessage.getId());
        chatRepository.save(chat);
        // 3. Send real-time message
        messagingTemplate.convertAndSendToUser(
                savedMessage.getReceiverId().toString(),
                "/queue/messages",
                savedMessage
        );

        return savedMessage;
    }

    @Override
    public List<Message> findChatsMessages(Integer chatId) {
        Optional<Chat> opt=chatRepository.findById(chatId);
        if(opt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
        }
        return messageRepository.findByChatId(chatId);
    }

    @Override
    public void deleteMessageById(Integer messageId) {
        Message message = messageRepository.findMessgeById(messageId);
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        Optional<Chat> opt=chatRepository.findById(message.getChatId());
        if(opt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found");
        }

        Chat chat = opt.get();
        chat.getMessageIds().remove(messageId);
        chatRepository.save(chat);
        messageRepository.deleteById(messageId);
    }
}
