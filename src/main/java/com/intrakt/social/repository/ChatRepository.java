package com.intrakt.social.repository;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE c.userId1 = :userId OR c.userId2 = :userId")
    List<Chat> findByUsersId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Chat c WHERE c.userId1 = :userId1 AND c.userId2 = :userId2 OR c.userId1 = :userId2 AND c.userId2 = :userId1")
    Chat findChatByUsersId(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);
}
