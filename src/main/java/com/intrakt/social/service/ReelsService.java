package com.intrakt.social.service;

import com.intrakt.social.models.Reels;
import com.intrakt.social.models.User;
import com.intrakt.social.request.ReelsRequest;

import java.util.List;

public interface ReelsService {

    Reels createReel(ReelsRequest reel, User user);

    List<Reels> findAllReels();

    List<Reels> findUsersReel(Integer userId);

    void deleteReelsById(Integer reelsId);
}
