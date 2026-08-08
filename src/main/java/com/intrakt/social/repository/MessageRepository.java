package com.intrakt.social.repository;

import com.intrakt.social.models.Message;
import lombok.Locked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    public List<Message> findByChatId(Integer chatId);

    public Message findMessgeById(Integer messageId);
}
