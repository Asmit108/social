package com.intrakt.social.controller;

import com.intrakt.social.exceptions.UserException;
import com.intrakt.social.models.Reels;
import com.intrakt.social.models.User;
import com.intrakt.social.service.ReelsService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReelsController {

    @Autowired
    private ReelsService reelsService;

    private UserService userService;

    @PostMapping("/api/reels")
    public Reels createReels(@RequestBody Reels reels, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserByJwt(jwt);
        Reels createdReels = reelsService.createReel(reels,user);

        return createdReels;
    }

    @GetMapping("/api/reels")
    public List<Reels> findAllReels(){
        List<Reels> reels = reelsService.findAllReels();
        return reels;
    }

    @GetMapping("/api/reels/user/{userId}")
    public List<Reels> findUsersReels(@PathVariable("userId") Integer userId) throws Exception {
        List<Reels> reels = reelsService.findUsersReel(userId);
        return reels;
    }

}
