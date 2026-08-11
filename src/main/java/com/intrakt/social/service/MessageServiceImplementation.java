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

@Service
public class MessageServiceImplementation implements MessageService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final ChatRepository chatRepository;

    @Autowired
    public MessageServiceImplementation(SimpMessagingTemplate simpMessagingTemplate, MessageRepository messageRepository, ChatService chatService, ChatRepository chatRepository) {
        this.messagingTemplate = simpMessagingTemplate;
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.chatRepository = chatRepository;
    }

    @Override
    public Message createMessage(Integer userId, Integer chatId, MessageRequest messageRequest) {
        Message message = new Message();
        Chat chat = chatService.findChatById(chatId);
        message.setChatId(chatId);
        message.setSenderId(userId);
        if(chat.getUserId1().equals(userId)) {
            message.setReceiverId(chat.getUserId2());
        } else if(chat.getUserId2().equals(userId)) {
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
        chatService.findChatById(chatId);
        return messageRepository.findByChatId(chatId);
    }

    @Override
    public void deleteMessageById(Integer messageId) {
        Message message = messageRepository.findMessgeById(messageId);
        if (message == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
        }
        Chat chat = chatService.findChatById(message.getChatId());
        chat.getMessageIds().remove(messageId);
        messageRepository.deleteById(messageId);
    }
}
