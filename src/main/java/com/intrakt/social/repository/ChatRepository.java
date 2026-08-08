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

    List<Chat> findByUsersId(Integer userId);

    @Query("SELECT c FROM Chat c WHERE :user Member of c.users AND :reqUser Member of c.users")
    Chat findChatByUsersId(@Param("user") User user, @Param("reqUser") User reqUser);
}
