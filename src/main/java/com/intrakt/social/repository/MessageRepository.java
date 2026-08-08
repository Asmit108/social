package com.intrakt.social.repository;

import com.intrakt.social.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByChatId(Integer chatId);

    Message findMessgeById(Integer messageId);
}
