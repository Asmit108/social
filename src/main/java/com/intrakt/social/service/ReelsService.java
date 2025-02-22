package com.intrakt.social.service;

import com.intrakt.social.models.Reels;
import com.intrakt.social.models.User;

import java.util.List;

public interface ReelsService {

    public Reels createReel(Reels reel, User user);

    public List<Reels> findAllReels();

    public List<Reels> findUsersReel(Integer userId) throws Exception;
}
