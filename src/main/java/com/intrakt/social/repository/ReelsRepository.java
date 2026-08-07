package com.intrakt.social.repository;

import com.intrakt.social.models.Reels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReelsRepository extends JpaRepository<Reels,Integer> {
    public List<Reels> findByUserId(Integer userId);

    @Query("SELECT r FROM Reels r")
    public List<Reels> findAllReels();

    public Reels findReelsById(Integer reelsId);
}
