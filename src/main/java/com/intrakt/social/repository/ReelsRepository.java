package com.intrakt.social.repository;

import com.intrakt.social.models.Reels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReelsRepository extends JpaRepository<Reels,Integer> {
    List<Reels> findByUserId(Integer userId);

    @Query("SELECT r FROM Reels r")
    List<Reels> findAllReels();

    Reels findReelsById(Integer reelsId);
}
