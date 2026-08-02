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

    private final ReelsService reelsService;
    private final UserService userService;

    @Autowired
    public ReelsController(ReelsService reelsService, UserService userService) {
        this.reelsService = reelsService;
        this.userService = userService;
    }

    @PostMapping("/api/reels")
    public Reels createReels(@RequestBody Reels reels, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserByJwt(jwt);
        return reelsService.createReel(reels,user);
    }

    @GetMapping("/api/reels")
    public List<Reels> findAllReels(){
        return reelsService.findAllReels();
    }

    @GetMapping("/api/reels/user/{userId}")
    public List<Reels> findUsersReels(@PathVariable("userId") Integer userId) throws Exception {
        return reelsService.findUsersReel(userId);
    }
}
