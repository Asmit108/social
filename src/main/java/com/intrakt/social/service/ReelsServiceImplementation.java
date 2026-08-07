package com.intrakt.social.service;

import com.intrakt.social.models.Reels;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.ReelsRepository;
import com.intrakt.social.request.ReelsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReelsServiceImplementation implements ReelsService {

    @Autowired
    private ReelsRepository reelsRepository;

    @Autowired
    private UserService userService;

    @Override
    public Reels createReel(ReelsRequest reel, User user) {
        Reels newReels=new Reels();
        newReels.setTitle(reel.getTitle());
        newReels.setVideo(reel.getVideo());
        newReels.setUser(user);
        return reelsRepository.save(newReels);
    }

    @Override
    public List<Reels> findAllReels() {
        return reelsRepository.findAllReels();
    }

    @Override
    public List<Reels> findUsersReel(Integer userId) {
        userService.findUserById(userId);
        return reelsRepository.findByUserId(userId);
    }

    @Override
    public void deleteReelsById(Integer reelsId) {
        Reels reels = reelsRepository.findReelsById(reelsId);
        if (reels == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reels not found");
        }
        reelsRepository.deleteById(reelsId);
    }
}
