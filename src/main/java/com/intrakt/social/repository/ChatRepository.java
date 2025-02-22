package com.intrakt.social.repository;

import com.intrakt.social.models.Chat;
import com.intrakt.social.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Integer> {

    public List<Chat> findByUsersId(Integer userId);

    @Query("SELECT c FROM Chat c WHERE :user Member of c.users AND :reqUser Member of c.users")
    public Chat findChatByUsersId(@Param("user") User user, @Param("reqUser") User reqUser);
}
