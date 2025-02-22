package com.intrakt.social.service;

import com.intrakt.social.models.Reels;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.ReelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReelsServiceImplementation implements ReelsService {

    @Autowired
    private ReelsRepository reelsRepository;

    @Autowired
    private UserService userService;

    @Override
    public Reels createReel(Reels reel, User user) {
        Reels newReels=new Reels();
        newReels.setTitle(reel.getTitle());
        newReels.setVideo(reel.getVideo());
        newReels.setUser(reel.getUser());
        return reelsRepository.save(newReels);
    }

    @Override
    public List<Reels> findAllReels() {
        return List.of();
    }

    @Override
    public List<Reels> findUsersReel(Integer userId) throws Exception {
        User user = userService.findUserById(userId);
        return reelsRepository.findByUserId(userId);
    }
}
